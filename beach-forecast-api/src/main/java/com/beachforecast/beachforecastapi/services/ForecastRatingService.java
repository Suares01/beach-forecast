package com.beachforecast.beachforecastapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.entities.CriterionEntity;
import com.beachforecast.beachforecastapi.entities.RatingEntity;
import com.beachforecast.beachforecastapi.entities.BeachEntity.Position;
import com.beachforecast.beachforecastapi.entities.CriterionEntity.CriterionType;
import com.beachforecast.beachforecastapi.entities.RatingEntity.RatingType;
import com.beachforecast.beachforecastapi.services.clients.Client.ForecastPoint;

@Service
public class ForecastRatingService {

  public Long evaluateConditions(ForecastPoint forecastPoint, BeachEntity beach, RatingEntity rating) {
    Double totalRating = (double) 0;
    Double totalWeight = (double) 0;

    List<CriterionEntity> criteria = rating.getCriteria();

    for (CriterionEntity criterion : criteria) {
      CriterionType criterionType = criterion.getType();
      Double criterionWeight = criterion.getWeight();
      Double criterionRating = (double) 0;

      switch (criterionType) {
        case WAVE_HEIGHT:
          criterionRating = evaluateWaveHeight(forecastPoint.getWaveHeight());
          break;
        case WAVE_DIRECTION:
          criterionRating = evaluateWaveDirection(forecastPoint.getWaveDirection(), beach);
          break;
        case WAVE_PERIOD:
          criterionRating = evaluateWavePeriod(forecastPoint.getWavePeriod());
          break;
        case SWELL_HEIGHT:
          criterionRating = evaluateWaveHeight(forecastPoint.getSwellHeight());
          break;
        case SWELL_DIRECTION:
          criterionRating = evaluateWaveDirection(forecastPoint.getWaveDirection(), beach);
          break;
        case SWELL_PERIOD:
          criterionRating = evaluateWavePeriod(forecastPoint.getWaveHeight());
          break;
        case WIND_DIRECTION:
          criterionRating = evaluateWindDirection(forecastPoint.getWindDirection(), forecastPoint.getWaveDirection(),
              beach);
          break;
      }

      Double adjustedRating = rating.getType() == RatingType.SURFER ? criterionRating : (5 - criterionRating);

      totalRating += adjustedRating * criterionWeight;
      totalWeight += criterionWeight;
    }

    if (totalWeight == 0) {
      return 0L;
    }

    Double finalRating = totalRating / totalWeight;
    return Math.round(finalRating);
  }

  private Double evaluateWaveHeight(Double waveHeight) {
    if (waveHeight < 0.5) {
      return 1.0;
    } else if (waveHeight < 1) {
      return 2.0;
    } else if (waveHeight < 1.5) {
      return 3.0;
    } else if (waveHeight < 2) {
      return 4.0;
    } else {
      return 5.0;
    }
  }

  private Double evaluateWaveDirection(Integer direction, BeachEntity beach) {
    Boolean isAligned = this.isAligned(direction, beach);

    return isAligned ? 5.0 : 1;
  }

  private Double evaluateWavePeriod(Double swellPeriod) {
    if (swellPeriod >= 12) {
      return 5.0;
    } else if (swellPeriod >= 10) {
      return 4.0;
    } else if (swellPeriod >= 8) {
      return 3.0;
    } else if (swellPeriod >= 6) {
      return 2.0;
    } else {
      return 1.0;
    }
  }

  private Double evaluateWindDirection(Integer windDirection, Integer waveDirection, BeachEntity beach) {
    Position wavePosition = getPositionFromDirection(waveDirection);
    Position windPosition = getPositionFromDirection(windDirection);
    Boolean isWindOffshore = isWindOffshore(wavePosition, windPosition, beach);

    if (wavePosition == windPosition) {
      return 1.0;
    } else if (isWindOffshore) {
      return 5.0;
    } else {
      return 2.0;
    }
  }

  private Boolean isAligned(Integer direction, BeachEntity beach) {
    Position positionFromDirection = getPositionFromDirection(direction);
    Position beachPosition = beach.getPosition();

    return (beachPosition == Position.NORTH && positionFromDirection == Position.NORTH)
        || (beachPosition == Position.SOUTH && positionFromDirection == Position.SOUTH)
        || (beachPosition == Position.EAST && positionFromDirection == Position.EAST)
        || (beachPosition == Position.WEST && positionFromDirection == Position.WEST);
  }

  private Position getPositionFromDirection(Integer direction) {
    if (direction <= 45) {
      return Position.NORTH;
    } else if (direction <= 135) {
      return Position.EAST;
    } else if (direction <= 225) {
      return Position.SOUTH;
    } else if (direction <= 315) {
      return Position.WEST;
    } else {
      return Position.NORTH;
    }
  }

  private Boolean isWindOffshore(Position wavePosition, Position windPosition, BeachEntity beach) {
    Position beachPosition = beach.getPosition();

    return (wavePosition == Position.NORTH && windPosition == Position.SOUTH && beachPosition == Position.NORTH)
        || (wavePosition == Position.SOUTH && windPosition == Position.NORTH && beachPosition == Position.SOUTH)
        || (wavePosition == Position.EAST && windPosition == Position.WEST && beachPosition == Position.EAST)
        || (wavePosition == Position.WEST && windPosition == Position.EAST && beachPosition == Position.WEST);
  }
}
