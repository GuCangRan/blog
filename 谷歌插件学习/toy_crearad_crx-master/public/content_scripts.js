/* eslint-disable no-undef */

chrome.runtime.onMessage.addListener(function(request, sender, sendResponse){
  console.log(request, sender, sendResponse)
})