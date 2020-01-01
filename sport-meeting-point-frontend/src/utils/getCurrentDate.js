export default function getCurrentDate  (separator='-'){
  const currentDate = new Date()
  const day = currentDate.getDate()
  const month = currentDate.getMonth() + 1
  const year = currentDate.getFullYear()

  return year + separator + month + separator + day;
}