import React, { useCallback, useState } from 'react'
import { Text, TextInput, StyleSheet } from 'react-native'
import Button from '@components/Button'

import stylesCommon from '@styles'

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
    <>
      <Text style={stylesCommon.title}>Connect to server</Text>
      <TextInput
        style={stylesCommon.textInput}
        placeholder="Server name or IP"
        placeholderTextColor="gray"
        onChangeText={setServer}
        value={server}
      />
      {!!serverError && (
        <Text style={stylesCommon.textError}>{serverError}</Text>
      )}
      <Button style={styles.button} onPress={handlePressConnect} title="Connect" />
    </>
  )
}

const styles = StyleSheet.create({
  button: {
    marginTop: 15,
  },
})
