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
    "title": "Edit Translation",
    "contexts": ["all"],
});

let translationItem = null

chrome.runtime.onMessage.addListener(function (request, sender, sendResponse) {

    if (request) {
        console.log(request)
        if (request.innerText && request.innerText.length > 3) {
            translationItem = request
            chrome.contextMenus.update("findTranslationMenuItem", {
                enabled: true,
                title: "Edit Translation '" + request.innerText + "'"
            });
        } else {
            translationItem = null
            chrome.contextMenus.update("findTranslationMenuItem", {
                enabled: false,
                title: "Edit Translation"
            });
        }
    }
});

chrome.contextMenus.onClicked.addListener(function (info, tab) {
    if (info.menuItemId !== "findTranslationMenuItem")
        return;

    if (translationItem)
        alert('go to translation -> ' + translationItem.innerText)

});
