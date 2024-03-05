import React, { forwardRef, useCallback, useEffect, useImperativeHandle, useRef } from 'react'
import {
  Platform,
  requireNativeComponent,
  UIManager,
  findNodeHandle
} from 'react-native'
import PropTypes from 'prop-types'

const IS_IOS = Platform.OS === 'ios'
const IS_ANDROID = Platform.OS === 'android'

const NATIVE_COMPONENT_NAME = IS_IOS ? 'RNTrueConfSdk' : 'TrueConfSDKViewManager'
const NativeComponent = requireNativeComponent(NATIVE_COMPONENT_NAME)

function TrueConfWrapper(props, ref) {
  const innerRef = useRef()

  const {
    isMicMuted = false,
    isCameraMuted = false,
    isAudioMuted = false,
    ...rest
  } = props

  const callCommand = useCallback((command, args) => {
    let cmd = (
      IS_IOS
        ? UIManager.getViewManagerConfig(NATIVE_COMPONENT_NAME)
        : UIManager[NATIVE_COMPONENT_NAME]
    ).Commands[command]
    if (cmd == null) {
      console.warn(`TrueConfWrapper: command '${command}' not found`)
      return
    }

    if (IS_ANDROID)
      cmd = cmd.toString()

    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      cmd,
      args
    )
  }, [])

  const initSdk = useCallback(() => {
    callCommand(
      'initSdk',
      []
    )
  }, [callCommand])

  const stopSdk = useCallback(() => {
    callCommand(
      'stopSdk',
      []
    )
  }, [callCommand])

  const makeCall = useCallback(to => {
    callCommand(
      'makeCall',
      [to]
    )
  }, [callCommand])

  const hangup = useCallback((forAll = true) => {
    callCommand(
      'hangup',
      [forAll]
    )
  }, [callCommand])

  const acceptCall = useCallback(accept => {
    callCommand(
      'acceptCall',
      [accept]
    )
  }, [callCommand])

  const joinConf = useCallback(confId => {
    callCommand(
      'joinConf',
      [confId]
    )
  }, [callCommand])

  const login = useCallback(({ userId, password, encryptPassword, enableAutoLogin }) => {
    callCommand(
      'login',
      [userId, password, encryptPassword, enableAutoLogin]
    )
  }, [callCommand])

  const logout = useCallback(() => {
    callCommand(
      'logout',
      []
    )
  }, [callCommand])

  const showCallWindow = useCallback(() => {
    if (!IS_ANDROID) return

    callCommand(
      'showCallWindow',
      []
    )
  }, [callCommand])

  const createFragment = useCallback(() => {
    if (!IS_ANDROID) return

    const viewId = findNodeHandle(innerRef.current)
    callCommand('create', [viewId])
  }, [])

  useEffect(() => {
    createFragment()
  }, [])

  useImperativeHandle(ref, () => ({
    initSdk,
    stopSdk,
    makeCall,
    hangup,
    acceptCall,
    joinConf,
    login,
    logout,
    showCallWindow,
  }))

  return (
    <NativeComponent
      {...rest}
      isMicMuted={isMicMuted}
      isCameraMuted={isCameraMuted}
      isAudioMuted={isAudioMuted}
      ref={innerRef}
    />
  )
}

TrueConfWrapper = forwardRef(TrueConfWrapper)

TrueConfWrapper.propTypes = {
  server: PropTypes.string,
  isMicMuted: PropTypes.bool,
  isCameraMuted: PropTypes.bool,
  isAudioMuted: PropTypes.bool,

  onServerStatus: PropTypes.func,
  onStateChanged: PropTypes.func,
  onLogin: PropTypes.func,
  onLogout: PropTypes.func,
  onAccept: PropTypes.func,
  onInvite: PropTypes.func,
  onReject: PropTypes.func,
  onRejectTimeout: PropTypes.func,
  onRecordRequest: PropTypes.func,
  onConferenceStart: PropTypes.func,
  onConferenceEnd: PropTypes.func,
  onUserStatusUpdate: PropTypes.func,
  onPressButton: PropTypes.func,
}

export default TrueConfWrapper
