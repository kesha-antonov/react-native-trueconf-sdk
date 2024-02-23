import React, { forwardRef, useCallback, useImperativeHandle, useRef } from 'react'
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle
} from 'react-native'
import PropTypes from 'prop-types'

const TRUE_CONF_VIEW_NATIVE_NAME = 'RNTrueconfReactSdk'
const RNTrueconfReactSdk = requireNativeComponent(TRUE_CONF_VIEW_NATIVE_NAME)

function TrueConfWrapper (props, ref) {
  const innerRef = useRef()

  const {
    isMuted = false,
    isCameraOn = true,
  } = props

  const callCommand = useCallback((command, args) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands[command],
      args
    )
  }, [])

  const initSdk = useCallback(() => {
    callCommand(
      'initSdk',
      [isMuted, isCameraOn]
    )
  }, [callCommand, isMuted, isCameraOn])

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

  useImperativeHandle(ref, () => ({
    initSdk,
    stopSdk,
    makeCall,
    hangup,
    acceptCall,
    joinConf,
    login,
    logout,
  }))

  return (
    <RNTrueconfReactSdk
      {...props}
      ref={innerRef}
    />
  )
}

TrueConfWrapper = forwardRef(TrueConfWrapper)

TrueConfWrapper.propTypes = {
  server: PropTypes.string,
  isMuted: PropTypes.bool,
  isCameraOn: PropTypes.bool,

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
}

export default TrueConfWrapper
