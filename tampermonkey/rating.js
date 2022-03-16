// ==UserScript==
// @name         Codeforces rating
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        https://codeforces.com/ratings/page/*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=codeforces.com
// @grant        none
// ==/UserScript==

(function() {
    var maxCount = 0;
    var counts = [];
    document.querySelectorAll("table")[5].querySelectorAll("tr").forEach(tr => {
        var tds = tr.querySelectorAll("td");
        if (tds.length >= 3) {
            var count = Number(tds[2].innerText);
            counts.push({ handle: tds[1].innerText, count: count });
            if (count > maxCount) {
                maxCount = count;
            }
        }
    });
    console.log("max count: " + maxCount);
    counts.sort( (a, b) => a.count - b.count );
    for (var i = counts.length - 10; i < counts.length; i++) {
        console.log(counts[i].handle + ": " + counts[i].count + ", https://codeforces.com/profile/" + counts[i].handle);
    }
})();