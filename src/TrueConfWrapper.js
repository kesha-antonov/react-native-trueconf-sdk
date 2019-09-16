import React, { createRef, Component } from 'react'
import {
  View,
  requireNativeComponent,
  UIManager,
  findNodeHandle
} from 'react-native'
import PropTypes from 'prop-types'
const TRUE_CONF_VIEW_NATIVE_NAME = 'RNTrueconfReactSdk'
const RNTrueconfReactSdk = requireNativeComponent(TRUE_CONF_VIEW_NATIVE_NAME, TrueConfWrapper)

class TrueConfWrapper extends Component {
  ref = createRef()

  initSdk = async () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.initSdk,
      []
    )
  }

  stopSdk = () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.stopSdk,
      []
    )
  }

  makeCall = async (to) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.makeCall,
      [to]
    )
  }

  hangup = async () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.hangup,
      []
    )
  }

  acceptCall = async (accept) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.acceptCall,
      [accept]
    )
  }

  joinConf = async (confId) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.joinConf,
      [confId]
    )
  }

  login = async ({ userId, password, encryptPassword, enableAutoLogin }) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.login,
      [userId, password, encryptPassword, enableAutoLogin]
    )
  }

  logout = () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.logout,
      []
    )
  }

  render () {
    return (
      <RNTrueconfReactSdk
        {...this.props}
        ref={this.ref}
      />
    )
  }
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
