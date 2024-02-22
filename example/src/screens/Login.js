import React, { useCallback, useState } from 'react'
import { Text, View, TextInput, Button } from 'react-native'

export default function Login (props) {
  const {
    onLogin,
    onDisconnect,
  } = props

  const [login, setLogin] = useState('sdk_test1')
  const [loginError, setLoginError] = useState()
  const [password, setPassword] = useState('11')
  const [passwordError, setPasswordError] = useState()

  const handleChangeLogin = useCallback(login => {
    setLogin(login)
    setLoginError(login.trim() ? null : 'Login is required')
  }, [])

  const handleChangePassword = useCallback(password => {
    setPassword(password)
    setPasswordError(password.trim() ? null : 'Password is required')
  }, [])

  const handlePressLogin = useCallback(() => {
    if (loginError) return
    if (passwordError) return

    onLogin(login, password)
  }, [onLogin, login, password, loginError, passwordError])

  return (
    <View style={{ padding: 20 }}>
      <Text style={{ fontSize: 27, color: 'black' }}>Login on server</Text>
      <TextInput
        style={{
          height: 40,
          borderBottomColor: 'gray',
          borderBottomWidth: 1,
          color: 'black',
        }}
        placeholder="Username"
        placeholderTextColor="gray"
        onChangeText={handleChangeLogin}
        value={login}
      />
      {loginError && (
        <Text style={{ color: 'red' }}>{loginError}</Text>
      )}
      <TextInput
        style={{
          height: 40,
          borderBottomColor: 'gray',
          borderBottomWidth: 1,
          color: 'black',
        }}
        placeholder="Password"
        placeholderTextColor="gray"
        secureTextEntry={true}
        onChangeText={handleChangePassword}
        value={password}
      />
      {passwordError && (
        <Text style={{ color: 'red' }}>{passwordError}</Text>
      )}
      <View style={{ margin: 7 }} />
      <Button onPress={handlePressLogin} title="Login" />
      <View style={{ margin: 7 }} />
      <Button onPress={onDisconnect} title="Change server" />
    </View>
  )
}
