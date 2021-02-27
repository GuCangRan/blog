import lunar from "./lunar";
let formatDate = function(time, format) {
  let date = time ? new Date(time) : new Date();
  let year = date.getFullYear();
  let month = date.getMonth() + 1; // 月份是从0开始的
  let day = date.getDate();
  let hour = date.getHours();
  let min = date.getMinutes();
  let sec = date.getSeconds();
  let preArr = Array.apply(null, Array(10)).map(function(elem, index) {
    return "0" + index;
  }); /// /开个长度为10的数组 格式为 00 01 02 03

  let newTime = format
    .replace(/YY/g, year)
    .replace(/F/g, hour >= 12 ? "pm" : "am")
    .replace(/ss/g, preArr[sec] || sec)
    .replace(/mm/g, preArr[min] || min)
    .replace(
      /hh/g,
      hour > 12 && format.includes("F")
        ? hour - 12
        : format.includes("F")
        ? hour
        : preArr[hour] || hour
    )
    .replace(/DD/g, preArr[day] || day)
    .replace(/MM/g, preArr[month] || month);

  return newTime;
};
export { lunar, formatDate };
