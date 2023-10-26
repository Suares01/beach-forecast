import { useDispatch as useReduxDispatch } from 'react-redux';
import { ReduxDispatch } from '../store';

export const useDispatch = () => useReduxDispatch<ReduxDispatch>();
