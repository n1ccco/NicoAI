export const toTimeAgo = (time: Date): string => {
  const now = new Date()
  const timeSinceCreation = now.getTime() - time.getTime()

  const seconds = Math.floor(timeSinceCreation / 1000)
  if (seconds < 60) {
    return `${seconds} second${seconds !== 1 ? 's' : ''} ago`
  }

  const minutes = Math.floor(seconds / 60)
  if (minutes < 60) {
    return `${minutes} minute${minutes !== 1 ? 's' : ''} ago`
  }

  const hours = Math.floor(minutes / 60)
  if (hours < 24) {
    return `${hours} hour${hours !== 1 ? 's' : ''} ago`
  }

  return new Date(time).toISOString().slice(0, 10)
}
