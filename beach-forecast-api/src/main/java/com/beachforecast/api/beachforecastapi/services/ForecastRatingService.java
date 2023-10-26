package com.beachforecast.api.beachforecastapi.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.entities.Criterion;
import com.beachforecast.api.beachforecastapi.entities.Rating;
import com.beachforecast.api.beachforecastapi.entities.Beach.Position;
import com.beachforecast.api.beachforecastapi.entities.Criterion.CriterionType;
import com.beachforecast.api.beachforecastapi.entities.Rating.RatingType;
import com.beachforecast.api.beachforecastapi.models.ForecastPoint;

@Service
public class ForecastRatingService {
  public int evaluateConditions(ForecastPoint forecastPoint, Beach beach, Rating rating) {
    float totalRating = 0;
    float totalWeight = 0;

    Set<Criterion> criteria = rating.getCriteria();

    for (Criterion criterion : criteria) {
      CriterionType criterionType = criterion.getType();
      float criterionWeight = criterion.getWeight();
      float criterionRating = 0;

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
          criterionRating = evaluateWindDirection(forecastPoint.getWindDirection(), forecastPoint.getWaveDirection(), beach);
          break;
      }

      float adjustedRating = rating.getType() == RatingType.SURFER ? criterionRating : (5 - criterionRating);

      totalRating += adjustedRating * criterionWeight;
      totalWeight += criterionWeight;
    }

    if (totalWeight == 0) {
      return 0;
    }

    float finalRating = totalRating / totalWeight;
    return Math.round(finalRating);
  }

  private float evaluateWaveHeight(float waveHeight) {
    if (waveHeight < 0.5) {
      return 1;
    } else if (waveHeight < 1) {
      return 2;
    } else if (waveHeight < 1.5) {
      return 3;
    } else if (waveHeight < 2) {
      return 4;
    } else {
      return 5;
    }
  }

  private float evaluateWaveDirection(int direction, Beach beach) {
    Boolean isAligned = this.isAligned(direction, beach);

    return isAligned ? 5 : 1;
  }

  private float evaluateWavePeriod(float swellPeriod) {
    if (swellPeriod >= 12) {
      return 5;
    } else if (swellPeriod >= 10) {
      return 4;
    } else if (swellPeriod >= 8) {
      return 3;
    } else if (swellPeriod >= 6) {
      return 2;
    } else {
      return 1;
    }
  }

  private float evaluateWindDirection(int windDirection, int waveDirection, Beach beach) {
    Position wavePosition = getPositionFromDirection(waveDirection);
    Position windPosition = getPositionFromDirection(windDirection);
    Boolean isWindOffshore = isWindOffshore(wavePosition, windPosition, beach);

    if (wavePosition == windPosition) {
      return 1;
    } else if (isWindOffshore) {
      return 5;
    } else {
      return 3;
    }
  }

  private boolean isAligned(int direction, Beach beach) {
    Position positionFromDirection = getPositionFromDirection(direction);
    Position beachPosition = beach.getPosition();

    return (beachPosition == Position.NORTH && positionFromDirection == Position.NORTH)
        || (beachPosition == Position.SOUTH && positionFromDirection == Position.SOUTH)
        || (beachPosition == Position.EAST && positionFromDirection == Position.EAST)
        || (beachPosition == Position.WEST && positionFromDirection == Position.WEST);
  }

  private Position getPositionFromDirection(int direction) {
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

  private boolean isWindOffshore(Position wavePosition, Position windPosition, Beach beach) {
    Position beachPosition = beach.getPosition();

    return (wavePosition == Position.NORTH && windPosition == Position.SOUTH && beachPosition == Position.NORTH)
        || (wavePosition == Position.SOUTH && windPosition == Position.NORTH && beachPosition == Position.SOUTH)
        || (wavePosition == Position.EAST && windPosition == Position.WEST && beachPosition == Position.EAST)
        || (wavePosition == Position.WEST && windPosition == Position.EAST && beachPosition == Position.WEST);
  }
}
