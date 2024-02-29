import React, { useCallback, useState } from 'react'
import { Text, View, TextInput } from 'react-native'
import Button from '@components/Button'

import stylesCommon from '@styles'

export default function Login(props) {
  const {
    onLogin,
    onChangeServer,
  } = props

  const [login, setLogin] = useState('test_sdk1') // test_sdk2
  const [loginError, setLoginError] = useState()
  const [password, setPassword] = useState('LvAR8rVyeq')
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
    <>
      <Text style={stylesCommon.title}>Login on server</Text>
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
        <Text style={stylesCommon.textError}>{loginError}</Text>
      )}
      <TextInput
        style={stylesCommon.textInput}
        placeholder="Password"
        placeholderTextColor="gray"
        secureTextEntry={true}
        onChangeText={handleChangePassword}
        value={password}
      />
      {passwordError && (
        <Text style={stylesCommon.textError}>{passwordError}</Text>
      )}
      <View style={{ margin: 7 }} />
      <Button onPress={handlePressLogin} title="Login" />
      <View style={{ margin: 7 }} />
      <Button onPress={onChangeServer} title="Change server" />
    </>
  )
}
