import { StateDescriptor } from '@/utils/types/state'

type EffectStateType = 'success' | 'failed'

type EffectStateDescriptor<
  TState,
  TStateType extends EffectStateType,
> = StateDescriptor<TState, TStateType>

type DefaultErrorState = {
  error: string
}

export type EffectResult<TSuccessState = {}, TErrorState = DefaultErrorState> =
  | EffectStateDescriptor<TSuccessState, 'success'>
  | EffectStateDescriptor<TErrorState, 'failed'>
