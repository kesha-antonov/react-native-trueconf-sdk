import React, { useCallback, useState } from 'react'
import { Text, View, TextInput, Button } from 'react-native'

export default function Connect (props) {
  const { server: defaultServer, onPressConnect } = props

  const [server, setServer] = useState(defaultServer)
  const [serverError, setServerError] = useState()

  const handlePressConnect = useCallback(() => {
    if (server.trim() === '')
      setServerError('Server is required')
    else
      onPressConnect(server)
  }, [server, onPressConnect])

  return (
    <View style={{ padding: 20 }}>
      <Text style={{ fontSize: 27, color: 'black' }}>Connect to server</Text>
      <TextInput
        style={{
          height: 40,
          borderBottomColor: 'gray',
          borderBottomWidth: 1,
          color: 'black',
        }}
        placeholder="Server name or IP"
        placeholderTextColor="gray"
        onChangeText={setServer}
        value={server}
      />
      {!!serverError && (
        <Text style={{ color: 'red' }}>{serverError}</Text>
      )}
      <View style={{ margin: 7 }} />
      <Button onPress={handlePressConnect} title="Connect" />
    </View>
  )
}
