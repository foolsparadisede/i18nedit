//content script
var clickedEl = null;

document.addEventListener("mousedown", function (event) {
    clickedEl = event.target;

    chrome.runtime.sendMessage({className: clickedEl.className, innerText: clickedEl.innerText})

}, true);