//content script
var clickedEl = null;

document.addEventListener("mousedown", function (event) {
    clickedEl = event.target;

}, true);

chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {
    if (request == "getClickedEl") {


        sendResponse({className: clickedEl.className, innerText: clickedEl.innerText});
    }
});