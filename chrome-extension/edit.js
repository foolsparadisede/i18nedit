chrome.runtime.onInstalled.addListener(function () {
    alert('huch');
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

function clickHandler() {

}

let contextMenu = chrome.contextMenus.create({
    "id": "123123",
    "title": "Find Translation",
    "contexts": ["all"],
});

chrome.contextMenus.onClicked.addListener(function(info, tab) {
    alert(tab);

});
