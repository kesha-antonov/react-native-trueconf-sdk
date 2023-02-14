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

  const initSdk = useCallback(async () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.initSdk,
      []
    )
  }, [])

  const stopSdk = useCallback(() => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.stopSdk,
      []
    )
  }, [])

  const makeCall = useCallback(async to => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.makeCall,
      [to]
    )
  }, [])

  const hangup = useCallback(async (forAll = true) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.hangup,
      [forAll]
    )
  }, [])

  const acceptCall = useCallback(async accept => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.acceptCall,
      [accept]
    )
  }, [])

  const joinConf = useCallback(async confId => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.joinConf,
      [confId]
    )
  }, [])

  const login = useCallback(async ({ userId, password, encryptPassword, enableAutoLogin }) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.login,
      [userId, password, encryptPassword, enableAutoLogin]
    )
  }, [])

  const logout = useCallback(() => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(innerRef.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.logout,
      []
    )
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
  onConferenceStart: PropTypes.func,
  onConferenceEnd: PropTypes.func,
  onUserStatusUpdate: PropTypes.func,
}

TrueConfWrapper.defaultProps = {
  isMuted: false,
  isCameraOn: true
}

export default TrueConfWrapper
