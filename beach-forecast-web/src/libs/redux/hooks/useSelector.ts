import {
  useSelector as useReduxSelector,
  type TypedUseSelectorHook,
} from 'react-redux';
import { ReduxState } from '../store';

export const useSelector: TypedUseSelectorHook<ReduxState> = useReduxSelector;
