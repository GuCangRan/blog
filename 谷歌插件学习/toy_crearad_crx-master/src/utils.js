/* eslint-disable no-undef */
export function setStore(key, value) {
  return new Promise(resolve =>
    chrome.storage.local.set({ [key]: value }, resolve)
  );
}

export function getStore(key) {
  return new Promise(resolve => {
    chrome.storage.local.get(key, item => resolve(item[key]));
  });
}
