import React from 'react'
import { StyleSheet, Text, TouchableOpacity } from 'react-native'

export default function Button ({ onPress, style, title }) {
  console.log('Button', title)

  return (
    <TouchableOpacity
      onPress={() => {
        console.log('Button onPress')

        onPress()
      }}
      activeOpacity={0.6}
      style={[styles.button, style]}
    >
      <Text style={styles.text}>
        {title.toUpperCase()}
      </Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#2196F3',
    paddingHorizontal: 5,
    paddingVertical: 10,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
  },
  text: {
    color: '#fff',
  },
})
