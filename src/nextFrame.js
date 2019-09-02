async function nextFrame () {
  return await new Promise(requestAnimationFrame)
}

export default nextFrame
