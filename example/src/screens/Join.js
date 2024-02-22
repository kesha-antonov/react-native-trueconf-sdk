import React, { useCallback, useState } from 'react'
import { Text, View, TextInput, Button } from 'react-native'

export default function Join (props) {
  const {
    onJoin,
    onLogout,
    onHangup,
    onMic,
    onCam,
  } = props

  const [confId, setConfId] = useState('sdk_test')
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
        <View style={{ flex: 1, padding: 10 }}>
          <Button onPress={handlePressJoin} title="Join" />
        </View>
        <View style={{ flex: 1, padding: 10 }}>
          <Button onPress={onLogout} title="Logout" />
        </View>
      </View>
      <View
        style={{
          marginVertical: 10,
          borderBottomColor: 'black',
          borderBottomWidth: 1,
        }}
      />
      <View style={{ flexDirection: 'row' }}>
        <View style={{ flex: 1, padding: 10 }}>
          <Button title="Hangup" onPress={onHangup} />
        </View>
        <View style={{ flex: 1, padding: 10 }}>
          <Button title="Mic" onPress={onMic} />
        </View>
        <View style={{ flex: 1, padding: 10 }}>
          <Button title="Cam" onPress={onCam} />
        </View>
      </View>
    </View>
  )
}