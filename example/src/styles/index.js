import { StyleSheet } from 'react-native'

export default StyleSheet.create({
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

  title: {
    fontSize: 27,
    color: 'black',
  },

  textInput: {
    height: 40,
    borderBottomColor: 'gray',
    borderBottomWidth: 1,
    color: 'black',
  },

  textError: {
    color: 'red',
  },
})
