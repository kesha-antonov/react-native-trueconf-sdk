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
import TrueConf from 'react-native-trueconf-sdk'
import constants from '@src/constants'

import Join from '@components/ConnectInfo/Join'
import Login from '@components/ConnectInfo/Login'
import Connect from '@components/ConnectInfo/Connect'

import stylesCommon from '@styles'

const STATUSES = {
  disconnected: 'No connection',
  connected: 'Connected to',
  loggedIn: 'Logged in as',
}

export default function App() {
  const trueconfRef = useRef()

  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isConnected, setIsConnected] = useState(false)
  const [isInConference, setIsInConference] = useState(false)
  const [server, setServer] = useState(constants.CONNECT_INFO.server)
  const [status, setStatus] = useState(STATUSES.disconnected)

  const [isAudioMuted, setIsAudioMuted] = useState(false)
  const [isCameraMuted, setIsCameraMuted] = useState(false)
  const [isMicMuted, setIsMicMuted] = useState(false)

  const [currentUserId, setCurrentUserId] = useState('')

  const onServerStatus = useCallback(e => {
    console.log('onServerStatus', e.nativeEvent)

    const {
      isConnected,
    } = e.nativeEvent

    setIsConnected(isConnected)
  }, [])

  const onLogin = useCallback(e => {
    console.log('onLogin', e.nativeEvent)
    const { isLoggedIn, userId } = e.nativeEvent

    setIsLoggedIn(isLoggedIn)
    setCurrentUserId(userId)
  }, [])

  const onLogout = useCallback(e => {
    console.log('onLogout', e.nativeEvent)

    setIsLoggedIn(false)
  }, [])

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
    setIsInConference(true)
  }, [])

  const onConferenceEnd = useCallback(e => {
    console.log('onConferenceEnd', e.nativeEvent)
    setIsInConference(false)
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

  const handleJoinConf = useCallback(confId => {
    trueconfRef.current?.joinConf(confId)
  }, [])

  const handleLogin = useCallback((userId, password) => {
    console.log('Login handleLogin', userId, password)
    trueconfRef.current?.login({
      userId,
      password,
      encryptPassword: true,
      enableAutoLogin: false,
    })
  }, [])

  const handleDisconnectFromServer = useCallback(() => {
    console.log('handleDisconnectFromServer')

    trueconfRef.current?.stopSdk()
    setIsConnected(false)
  }, [])

  const handleLogout = useCallback(() => {
    trueconfRef.current?.logout()
  }, [])

  const handleHangup = useCallback(() => {
    const hangupForAll = true
    trueconfRef.current?.hangup(hangupForAll)
  }, [])

  const handleToggleMic = useCallback(() => {
    setIsMicMuted(isMicMuted => !isMicMuted)
  }, [])

  const handleToggleCamera = useCallback(() => {
    setIsCameraMuted(isCameraMuted => !isCameraMuted)
  }, [])

  const handleToggleAudio = useCallback(() => {
    setIsAudioMuted(isAudioMuted => !isAudioMuted)
  }, [])

  const loginOrConfId = useCallback(() => {
    if (isLoggedIn)
      return (
        <Join
          onJoin={handleJoinConf}
          onLogout={handleLogout}
          onHangup={handleHangup}
          onPressMic={handleToggleMic}
          onPressCamera={handleToggleCamera}
          onPressAudio={handleToggleAudio}
          onShowCallWindow={() => trueconfRef.current?.showCallWindow()}

          isMicMuted={isMicMuted}
          isCameraMuted={isCameraMuted}
          isAudioMuted={isAudioMuted}
          isInConference={isInConference}
        />
      )
    else if (isConnected)
      return (
        <Login
          onLogin={handleLogin}
          onChangeServer={handleDisconnectFromServer}
        />
      )
    else
      return (
        <Connect server={server} onPressConnect={handlePressConnect} />
      )
  }, [
    server,
    isLoggedIn,
    isConnected,
    isInConference,

    handleJoinConf,
    handleLogout,
    handleHangup,
    handleToggleMic,
    handleToggleCamera,
    handleToggleAudio,
    handlePressConnect,
    handleLogin,
    handleDisconnectFromServer,

    isMicMuted,
    isCameraMuted,
    isAudioMuted,
  ])

  const handlePressButton = useCallback(({ nativeEvent: { kind, isMuted } }) => {
    console.log('handlePressButton', { kind, isMuted })
    switch (kind) {
      case 'mic':
        setIsMicMuted(isMuted)
        break
      case 'camera':
        setIsCameraMuted(isMuted)
        break
      case 'audio':
        setIsAudioMuted(isMuted)
        break
    }
  }, [])

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

  console.log('App', { isLoggedIn, isConnected, status }, { isMicMuted, isCameraMuted, isAudioMuted })

  return (
    <SafeAreaView style={[stylesCommon.fill, styles.container]}>
      <StatusBar barStyle={'dark-content'} />

      {/* CONNECT/LOGIN/CONF ID */}
      <View style={styles.connectInfo}>
        {loginOrConfId()}
      </View>

      {/* TRUECONF CONF (VIEW + SELF-VIEW) */}
      <View style={[stylesCommon.fill, stylesCommon.relative, styles.tcViewContainer]}>
        <TrueConf
          ref={trueconfRef}
          style={[
            stylesCommon.fill,
            stylesCommon.absoluteFill,
            {
              backgroundColor: 'blue',
            }
          ]}

          server={server}
          isMicMuted={isMicMuted}
          isCameraMuted={isCameraMuted}
          isAudioMuted={isAudioMuted}
          isFadeTransitionEnabled

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
          onPressButton={handlePressButton}
        />
      </View>

      {/* STATUS */}
      <View style={styles.status}>
        <Text style={styles.statusText}>{`Status: ${status}`}</Text>
      </View>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'white',
  },

  connectInfo: {
    padding: 20,
  },

  tcViewContainer: {
    backgroundColor: 'grey',
  },

  status: {
    alignItems: 'center',
    paddingVertical: 10,
  },
  statusText: {
    color: '#000',
  },
})
