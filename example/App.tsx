import React, { useState, useRef, useCallback, useEffect } from 'react'
import {
  Text,
  View,
  Alert,
  SafeAreaView,
  StatusBar,
  Platform,
  StyleSheet,
} from 'react-native'

import TrueConf from 'react-native-trueconf-react-sdk'
import Join from './src/screens/Join'
import Login from './src/screens/Login'
import Connect from './src/screens/Connect'

const STATUSES = {
  disconnected: 'No connection',
  connected: 'Connected to',
  loggedIn: 'Logged in as',
}

export default function App () {
  const trueconfRef = useRef()

  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isConnected, setIsConnected] = useState(false)
  const [server, setServer] = useState('qa4.trueconf.net')
  const [status, setStatus] = useState(STATUSES.disconnected)
  const [isMuted, setIsMuted] = useState(false)
  const [isCameraOn, setIsCameraOn] = useState(true)
  const [currentUserId, setCurrentUserId] = useState('')

  const onServerStatus = useCallback(e => {
    console.log('onServerStatus', e.nativeEvent)

    const {
      isConnected,
      server,
    } = e.nativeEvent

    setIsConnected(isConnected)
  }, [])

  const onLogin = useCallback(e => {
    console.log('onLogin', e.nativeEvent)
    const { isLoggedIn, userId } = e.nativeEvent

    setIsLoggedIn(isLoggedIn)
    setCurrentUserId(userId)
  }, [server])

  const onLogout = useCallback(e => {
    console.log('onLogout', e.nativeEvent)

    setIsLoggedIn(false)
  }, [server])

  const onInvite = useCallback(e => {
    console.log('onInvite', e.nativeEvent)

    const {
      userId,
    } = e.nativeEvent

    if (Platform.OS === 'ios')
      Alert.alert(
        'Incoming call',
        'Accept incoming call from ' + userId,
        [
          {
            text: 'Reject',
            onPress: () => trueconfRef.current?.acceptCall(false),
            style: 'cancel',
          },
          { text: 'Accept', onPress: () => trueconfRef.current?.acceptCall(true) },
        ]
      )
  }, [])

  const onStateChanged = useCallback(e => {
    console.log('onStateChanged', e.nativeEvent)

    const {
      isConnectedToServer,
    } = e.nativeEvent

    setIsConnected(isConnectedToServer)
  }, [])

  const onAccept = useCallback(e => {
    console.log('onAccept', e.nativeEvent)
  }, [])

  const onReject = useCallback(e => {
    console.log('onReject', e.nativeEvent)
  }, [])

  const onRejectTimeout = useCallback(e => {
    console.log('onRejectTimeout', e.nativeEvent)
  }, [])

  const onConferenceStart = useCallback(e => {
    console.log('onConferenceStart', e.nativeEvent)
  }, [])

  const onConferenceEnd = useCallback(e => {
    console.log('onConferenceEnd', e.nativeEvent)
  }, [])

  const onUserStatusUpdate = useCallback(e => {
    console.log('onUserStatusUpdate', e.nativeEvent)
  }, [])

  const handlePressConnect = useCallback(server => {
    setServer(server)

    setTimeout(() => {
      trueconfRef.current?.initSdk()
    })
  }, [])

  const loginOrConfId = useCallback(() => {
    if (isLoggedIn)
      return (
        <Join
          onJoin={confId => trueconfRef.current?.joinConf(confId)}
          onLogout={() => trueconfRef.current?.logout()}
          onHangup={() => trueconfRef.current?.hangup(true)}
          onMic={() => setIsMuted(isMuted => !isMuted)}
          onCam={() => setIsCameraOn(isCameraOn => !isCameraOn)}
        />
      )
    else if (isConnected)
      return (
        <Login
          onLogin={(userId, password) => {
            console.log('Login onLogin', userId, password, !!trueconfRef.current)
            trueconfRef.current?.login({
              userId,
              password,
              encryptPassword: true,
              enableAutoLogin: true,
            })
          }}
          onDisconnect={() => setIsConnected(false)}
        />
      )
    else
      return (
        <Connect server={server} onPressConnect={handlePressConnect} />
      )
  }, [isLoggedIn, isConnected, server])

  useEffect(() => {
    let status
    if (isConnected)
      if (isLoggedIn)
        status = STATUSES.loggedIn + ' ' + currentUserId
      else
        status = STATUSES.connected + ' ' + server
    else
      status = STATUSES.disconnected

    setStatus(status)
  }, [isConnected, isLoggedIn, server, currentUserId])

  console.log('App', { isLoggedIn, isConnected })

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: 'white' }}>
      <StatusBar barStyle={'dark-content'} />

      {/* CONNECT/LOGIN/CONF ID */}
      <View>
        {loginOrConfId()}
      </View>

      {/* TRUECONF CONF (VIEW + SELF-VIEW) */}
      <View style={[styles.fill, styles.relative, {backgroundColor: 'grey' }]}>
        <TrueConf
          ref={trueconfRef}
          style={[styles.fill, styles.absoluteFill]}

          server={server}
          isMuted={isMuted}
          isCameraOn={isCameraOn}

          onServerStatus={onServerStatus}
          onLogin={onLogin}
          onLogout={onLogout}
          onInvite={onInvite}

          onStateChanged={onStateChanged}
          onAccept={onAccept}
          onReject={onReject}
          onRejectTimeout={onRejectTimeout}
          onConferenceStart={onConferenceStart}
          onConferenceEnd={onConferenceEnd}
          onUserStatusUpdate={onUserStatusUpdate}
        />
      </View>

      {/* STATUS */}
      <View style={{ alignItems: 'center', paddingVertical: 10 }}>
        <Text>{`Status: ${status}`}</Text>
      </View>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
  absoluteFill: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
  relative: {
    position: 'relative',
  },
})
