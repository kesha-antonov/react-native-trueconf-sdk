import React, { useCallback, useState } from 'react'
import { Text, View, TextInput, TouchableOpacity } from 'react-native'

function Button ({ title, onPress }) {
  return (
    <TouchableOpacity
      onPress={onPress}
      activeOpacity={0.6}
      style={{ backgroundColor: '#2196F3', flex: 1, margin: 5, paddingHorizontal: 5, paddingVertical: 10, justifyContent: 'center', alignItems: 'center', borderRadius: 10}}
    >
      <Text style={{color: '#fff'}}>
        {title.toUpperCase()}
      </Text>
    </TouchableOpacity>
  )
}

export default function Join (props) {
  const {
    onJoin,
    onLogout,
    onHangup,

    onPressMic,
    onPressCamera,
    onPressSpeaker,

    onShowCallWindow,

    isMuted,
    isCameraMuted,
    isSpeakerMuted,
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
    <View style={{ padding: 20 }}>
      <Text style={{ fontSize: 27, color: 'black' }}>Join the conference</Text>
      <TextInput
        style={{
          height: 40,
          borderBottomColor: 'gray',
          borderBottomWidth: 1,
          color: 'black',
        }}
        placeholder="Conference ID"
        placeholderTextColor="gray"
        onChangeText={setConfId}
        value={confId}
      />
      {!!confIdError && (
        <Text style={{ color: 'red' }}>{confIdError}</Text>
      )}
      <View style={{ margin: 7 }} />
      <View style={{ flexDirection: 'row' }}>
        <Button onPress={handlePressJoin} title="Join" />
        <Button onPress={onLogout} title="Logout" />
      </View>
      <View
        style={{
          marginVertical: 10,
          borderBottomColor: 'black',
          borderBottomWidth: 1,
        }}
      />

      <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
        <Button title={'Mic: ' + (isMuted ? 'off' : 'on')} onPress={onPressMic} />
        <Button title={'Cam: ' + (isCameraMuted ? 'off' : 'on')} onPress={onPressCamera} />
        <Button title={'Speaker: ' + (isSpeakerMuted ? 'off' : 'on')} onPress={onPressSpeaker} />

        {/* TODO: SHOW ONLY WHEN CONNECTED TO CONFERENCE/CALL */}
        <Button title='Hangup' onPress={onHangup} />
        <Button title='Show call window' onPress={onShowCallWindow} />
      </View>
    </View>
  )
}
