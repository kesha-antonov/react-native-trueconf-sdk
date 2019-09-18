import React, { Component, createRef } from 'react'
import { requireNativeComponent, UIManager, findNodeHandle } from 'react-native'

const TRUE_CONF_VIEW_NATIVE_NAME = 'RCTTrueConfSDKView'
const TrueConfViewNative = requireNativeComponent(TRUE_CONF_VIEW_NATIVE_NAME, TrueConfView)
console.log('TrueConfViewNative', TrueConfViewNative)

// TODO: MV TO IOS IMPLEMENTATION
class TrueConfView extends Component {
  ref = createRef()

  componentDidMount () {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.ref.current),
      UIManager.getViewManagerConfig(TRUE_CONF_VIEW_NATIVE_NAME).Commands.initSdk,
      []
    )
  }

  render() {
    return (
      <TrueConfViewNative ref={this.ref} style={{height: '80%', backgroundColor: 'green'}} />
    )
  }
}

export default TrueConfView
