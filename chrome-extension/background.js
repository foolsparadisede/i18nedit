chrome.runtime.onInstalled.addListener(function () {

});

chrome.declarativeContent.onPageChanged.removeRules(undefined, function () {
    chrome.declarativeContent.onPageChanged.addRules([{
        conditions: [new chrome.declarativeContent.PageStateMatcher({
            pageUrl: {hostEquals: 'app.alpas.io'},
        })
        ],
        actions: [new chrome.declarativeContent.ShowPageAction()]
    }]);
});

let contextMenu = chrome.contextMenus.create({
    "id": "findTranslationMenuItem",
    "title": "Find Translation",
    "contexts": ["all"],
});

chrome.contextMenus.onClicked.addListener(function (info, tab) {
    if (info.menuItemId !== "findTranslationMenuItem")
        return;

    chrome.tabs.sendMessage(tab.id, "getClickedEl", function (clickedEl) {
        if (clickedEl) {
            console.log(clickedEl)
            if (clickedEl.innerText && clickedEl.innerText.length > 3) {
                alert('go to translation -> ' + clickedEl.innerText)
            }
        }
    });

});
