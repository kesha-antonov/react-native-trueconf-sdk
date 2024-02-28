module.exports = function (api) {
  api.cache(true)

  return {
    presets: [
      'module:@react-native/babel-preset',
      '@babel/preset-typescript',
    ],
    plugins: [
      [
        'module-resolver',
        {
          root: ['./src'],
          alias: {
            '@src': './src',
            '@components': './src/components',
            '@styles': './src/styles',
          },
          extensions: ['.js', '.ts', '.tsx', '.json'],
        },
      ],
      '@babel/plugin-transform-flow-strip-types',
      [
        '@babel/plugin-transform-runtime',
        {
          helpers: true,
          regenerator: false,
        },
      ],
    ],
  }
}
