import React, { useRef, useCallback, useEffect } from 'react'
import { requireNativeComponent, UIManager, findNodeHandle } from 'react-native'

const TRUE_CONF_VIEW_MANAGER_NATIVE_NAME = 'RCTTrueConfSDKViewManager'
const TrueConfViewManagerNative = requireNativeComponent(TRUE_CONF_VIEW_MANAGER_NATIVE_NAME)
console.log('UIManager', UIManager)
console.log('TrueConfViewManagerNative', TrueConfViewManagerNative)

// TODO: MV TO IOS IMPLEMENTATION
function TrueConfView (props) {
  const ref = useRef()

  const createFragment = useCallback(() => {
    const viewId = findNodeHandle(ref.current)

    UIManager.dispatchViewManagerCommand(
      viewId,
      // UIManager.getViewManagerConfig(TRUE_CONF_VIEW_MANAGER_NATIVE_NAME).Commands.initSdk,
      UIManager[TRUE_CONF_VIEW_MANAGER_NATIVE_NAME].Commands.create.toString(),
      [viewId]
    )
  }, [])

  useEffect(() => {
    createFragment()
  }, [])

  return (
    <TrueConfViewManagerNative
      ref={ref}
      style={{height: '80%', backgroundColor: 'green'}}
    />
  )
}

export default TrueConfView
