import React, { useCallback, useState } from 'react'
import { Text, View, TextInput, StyleSheet } from 'react-native'

import Button from '@components/Button'

import stylesCommon from '@styles'

export default function Join (props) {
  const {
    onJoin,
    onLogout,
    onHangup,

    onPressMic,
    onPressCamera,
    onPressAudio,

    onShowCallWindow,

    isAudioMuted,
    isCameraMuted,
    isMicMuted,

    isInConference,
  } = props

  const [confId, setConfId] = useState('test_sdk')
  const [confIdError, setConfIdError] = useState()

  const handlePressJoin = useCallback(() => {
    if (confId.trim() === '')
      setConfIdError('Conf ID is required')
    else
      onJoin(confId)
  }, [confId, onJoin])

  return (
    <>
      <Text style={stylesCommon.title}>Join the conference</Text>
      <TextInput
        style={stylesCommon.textInput}
        placeholder="Conference ID"
        placeholderTextColor="gray"
        onChangeText={setConfId}
        value={confId}
      />
      {!!confIdError && (
        <Text style={stylesCommon.textError}>{confIdError}</Text>
      )}

      {
        !isInConference && (
          <>
            <View style={styles.buttons}>
              <Button style={[stylesCommon.fill, styles.button]} title="Join" onPress={handlePressJoin} />
              <Button style={[stylesCommon.fill, styles.button]} title="Logout" onPress={onLogout} />
            </View>
            <View style={styles.buttonsSeparator} />
          </>
        )
      }

      <View style={styles.buttons}>
        <Button style={[stylesCommon.fill, styles.button]} title={'Audio: ' + (isAudioMuted ? 'off' : 'on')} onPress={onPressAudio} />
        <Button style={[stylesCommon.fill, styles.button]} title={'Cam: ' + (isCameraMuted ? 'off' : 'on')} onPress={onPressCamera} />
        <Button style={[stylesCommon.fill, styles.button]} title={'Mic: ' + (isMicMuted ? 'off' : 'on')} onPress={onPressMic} />

        {
          isInConference && (
            <>
              <Button style={[stylesCommon.fill, styles.button]} title='Hangup' onPress={onHangup} />
              <Button style={[stylesCommon.fill, styles.button]} title='Show call window' onPress={onShowCallWindow} />
            </>
          )
        }
      </View>
    </>
  )
}

const styles = StyleSheet.create({
  buttons: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 7,
  },
  buttonsSeparator: {
    marginTop: 10,
    borderBottomColor: 'black',
    borderBottomWidth: 1,
  },
  button: {
    margin: 5,
  },
})
