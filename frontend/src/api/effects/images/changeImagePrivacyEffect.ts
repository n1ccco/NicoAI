import { axiosInstance } from '@/api/axios'
import { IMAGES } from '@/constants/apiConstants'
import type { EffectResult } from '../types'

const StateEmpty = {}

type ChangeImagePrivacyEffectType = (
  id: number,
  privacyPayload: { isPublic: boolean }
) => Promise<EffectResult>

const ChangeImagePrivacyErrorMessage = 'Can not change privacy of image'

const ChangeImagePrivacyEffect: ChangeImagePrivacyEffectType = async (
  id,
  privacyPayload
) => {
  const action = privacyPayload.isPublic ? 'makePublic' : 'makePrivate'

  return await axiosInstance
    .put(`${IMAGES}/${id}`, { action })
    .then((_res): EffectResult => {
      return {
        type: 'success',
        state: StateEmpty,
      }
    })
    .catch((_err) => {
      return {
        type: 'failed',
        state: {
          error: ChangeImagePrivacyErrorMessage,
        },
      }
    })
}

export default ChangeImagePrivacyEffect
