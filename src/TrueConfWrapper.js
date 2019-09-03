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
import nextFrame from './nextFrame'

class TrueConfWrapper extends Component {
  ref = createRef()

  componentDidMount() {
    setTimeout(this.initSdk)
  }

  initSdk = () => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.initSdk,
      []
    )
  }

  makeCall = async (to) => {
    await nextFrame()

    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.makeCall,
      [to]
    )
  }

  hangup = async () => {
    await nextFrame()
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.hangup,
      []
    )
  }

  login = async ({ userId, password, encryptPassword, enableAutoLogin }) => {
    await nextFrame()
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.login,
      [userId, password, encryptPassword, enableAutoLogin]
    )
  }

  onServerStatus = (e) => {
    if (!this.props.onServerStatus) return

    this.props.onServerStatus(e.nativeEvent)
  }

  onStateChanged = (e) => {
    if (!this.props.onStateChanged) return

    this.props.onStateChanged(e.nativeEvent)
  }

  onLogin = (e) => {
    if (!this.props.onLogin) return

    this.props.onLogin(e.nativeEvent)
  }

  render () {
    return (
      <RNTrueconfReactSdk
        {...this.props}
        ref={this.ref}
        onServerStatus={this.onServerStatus}
        onStateChanged={this.onStateChanged}
        onLogin={this.onLogin}
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
}
TrueConfWrapper.defaultProps = {
  muted: false,
  cameraOn: true
}

export default TrueConfWrapper
