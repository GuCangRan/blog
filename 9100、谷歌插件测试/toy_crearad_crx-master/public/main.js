/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */
document.addEventListener('DOMContentLoaded', function () {
  console.log('我被执行了main！');
  getItem('__book_adblock__').then(sites => {
    console.log(sites);
    sites.forEach(site => {
      if (location.href.includes(site)) {
        main();
      }
    })
  })
});

function getItem(key) {
  return new Promise(resolve => {
    chrome.storage.local.get(key, item => resolve(item[key]));
  });
}

function main() {
  const $imgs = document.querySelectorAll('img');
  const $bingo = document.querySelectorAll('bingo');
  Array.from($imgs).forEach(ele => {
    ele.remove();
  })
  Array.from($bingo).forEach(ele => {
    ele.remove();
  })
}
