import { Platform } from 'react-native'

export default {
  CONNECT_INFO: {
    server: 'video.trueconf.com',
    userId: 'test_sdk1', // test_sdk2
    password: 'LvAR8rVyeq',
    conferenceId: 'test_sdk',
  },
  IS_ANDROID: Platform.OS === 'android',
}
