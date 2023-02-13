import React, { useRef, useCallback, useImperativeHandle } from 'react'
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle
} from 'react-native'
import PropTypes from 'prop-types'

const TRUE_CONF_VIEW_NATIVE_NAME = 'RNTrueconfReactSdk'
const RNTrueconfReactSdk = requireNativeComponent(TRUE_CONF_VIEW_NATIVE_NAME, TrueConfWrapper)

function TrueConfWrapper (props) {
  const ref = useRef()

  const initSdk = useCallback(async () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.initSdk,
      []
    )
  }, [])

  const stopSdk = useCallback(() => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.stopSdk,
      []
    )
  }, [])

  const makeCall = useCallback(async to => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.makeCall,
      [to]
    )
  }, [])

  const hangup = useCallback(async (forAll = true) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.hangup,
      [forAll]
    )
  }, [])

  const acceptCall = useCallback(async accept => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.acceptCall,
      [accept]
    )
  }, [])

  const joinConf = useCallback(async confId => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.joinConf,
      [confId]
    )
  }, [])

  const login = useCallback(async ({ userId, password, encryptPassword, enableAutoLogin }) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.login,
      [userId, password, encryptPassword, enableAutoLogin]
    )
  }, [])

  const logout = useCallback(() => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
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
      ref={ref}
    />
  )
}

TrueConfWrapper.propTypes = {
  server: PropTypes.string,
  muted: PropTypes.bool,
  cameraOn: PropTypes.bool,

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
  muted: false,
  cameraOn: true
}

export default TrueConfWrapper
