/**
 * Created by gao on 2017/3/13.
 */
/*!!
 * Piwik - free/libre analytics platform
 *
 * JavaScript tracking client
 *
 * @link http://piwik.org
 * @source https://github.com/piwik/piwik/blob/master/js/piwik.js
 * @license http://piwik.org/free-software/bsd/ BSD-3 Clause (also in js/LICENSE.txt)
 * @license magnet:?xt=urn:btih:c80d50af7d3db9be66a4d0a86db0286e4fd33292&dn=bsd-3-clause.txt BSD-3-Clause
 */
if (typeof JSON2 !== "object" && typeof window.JSON === "object" && window.JSON.stringify && window.JSON.parse) {
    JSON2 = window.JSON
} else { (function() {
    var a = {};
    /*!! JSON v3.3.2 | http://bestiejs.github.io/json3 | Copyright 2012-2014, Kit Cambridge | http://kit.mit-license.org */
    (function() {
        var c = typeof define === "function" && define.amd;
        var e = {
            "function": true,
            object: true
        };
        var h = e[typeof a] && a && !a.nodeType && a;
        var i = e[typeof window] && window || this,
            b = h && e[typeof module] && module && !module.nodeType && typeof global == "object" && global;
        if (b && (b.global === b || b.window === b || b.self === b)) {
            i = b
        }
        function j(ab, V) {
            ab || (ab = i.Object());
            V || (V = i.Object());
            var K = ab.Number || i.Number,
                R = ab.String || i.String,
                x = ab.Object || i.Object,
                S = ab.Date || i.Date,
                T = ab.SyntaxError || i.SyntaxError,
                aa = ab.TypeError || i.TypeError,
                J = ab.Math || i.Math,
                Y = ab.JSON || i.JSON;
            if (typeof Y == "object" && Y) {
                V.stringify = Y.stringify;
                V.parse = Y.parse
            }
            var n = x.prototype,
                u = n.toString,
                r, m, L;
            var B = new S( - 3509827334573292);
            try {
                B = B.getUTCFullYear() == -109252 && B.getUTCMonth() === 0 && B.getUTCDate() === 1 && B.getUTCHours() == 10 && B.getUTCMinutes() == 37 && B.getUTCSeconds() == 6 && B.getUTCMilliseconds() == 708
            } catch(v) {}
            function o(ac) {
                if (o[ac] !== L) {
                    return o[ac]
                }
                var ad;
                if (ac == "bug-string-char-index") {
                    ad = "a" [0] != "a"
                } else {
                    if (ac == "json") {
                        ad = o("json-stringify") && o("json-parse")
                    } else {
                        var ak, ah = '{"a":[1,true,false,null,"\\u0000\\b\\n\\f\\r\\t"]}';
                        if (ac == "json-stringify") {
                            var ai = V.stringify,
                                aj = typeof ai == "function" && B;
                            if (aj) { (ak = function() {
                                return 1
                            }).toJSON = ak;
                                try {
                                    aj = ai(0) === "0" && ai(new K()) === "0" && ai(new R()) == '""' && ai(u) === L && ai(L) === L && ai() === L && ai(ak) === "1" && ai([ak]) == "[1]" && ai([L]) == "[null]" && ai(null) == "null" && ai([L, u, null]) == "[null,null,null]" && ai({
                                            a: [ak, true, false, null, "\x00\b\n\f\r\t"]
                                        }) == ah && ai(null, ak) === "1" && ai([1, 2], null, 1) == "[\n 1,\n 2\n]" && ai(new S( - 8640000000000000)) == '"-271821-04-20T00:00:00.000Z"' && ai(new S(8640000000000000)) == '"+275760-09-13T00:00:00.000Z"' && ai(new S( - 62198755200000)) == '"-000001-01-01T00:00:00.000Z"' && ai(new S( - 1)) == '"1969-12-31T23:59:59.999Z"'
                                } catch(ae) {
                                    aj = false
                                }
                            }
                            ad = aj
                        }
                        if (ac == "json-parse") {
                            var ag = V.parse;
                            if (typeof ag == "function") {
                                try {
                                    if (ag("0") === 0 && !ag(false)) {
                                        ak = ag(ah);
                                        var af = ak.a.length == 5 && ak.a[0] === 1;
                                        if (af) {
                                            try {
                                                af = !ag('"\t"')
                                            } catch(ae) {}
                                            if (af) {
                                                try {
                                                    af = ag("01") !== 1
                                                } catch(ae) {}
                                            }
                                            if (af) {
                                                try {
                                                    af = ag("1.") !== 1
                                                } catch(ae) {}
                                            }
                                        }
                                    }
                                } catch(ae) {
                                    af = false
                                }
                            }
                            ad = af
                        }
                    }
                }
                return o[ac] = !!ad
            }
            if (!o("json")) {
                var U = "[object Function]",
                    Q = "[object Date]",
                    N = "[object Number]",
                    O = "[object String]",
                    E = "[object Array]",
                    A = "[object Boolean]";
                var F = o("bug-string-char-index");
                if (!B) {
                    var s = J.floor;
                    var Z = [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334];
                    var D = function(ac, ad) {
                        return Z[ad] + 365 * (ac - 1970) + s((ac - 1969 + (ad = +(ad > 1))) / 4) - s((ac - 1901 + ad) / 100) + s((ac - 1601 + ad) / 400)
                    }
                }
                if (! (r = n.hasOwnProperty)) {
                    r = function(ae) {
                        var ac = {},
                            ad;
                        if ((ac.__proto__ = null, ac.__proto__ = {
                                toString: 1
                            },
                                ac).toString != u) {
                            r = function(ah) {
                                var ag = this.__proto__,
                                    af = ah in (this.__proto__ = null, this);
                                this.__proto__ = ag;
                                return af
                            }
                        } else {
                            ad = ac.constructor;
                            r = function(ag) {
                                var af = (this.constructor || ad).prototype;
                                return ag in this && !(ag in af && this[ag] === af[ag])
                            }
                        }
                        ac = null;
                        return r.call(this, ae)
                    }
                }
                m = function(ae, ah) {
                    var af = 0,
                        ac, ad, ag; (ac = function() {
                        this.valueOf = 0
                    }).prototype.valueOf = 0;
                    ad = new ac();
                    for (ag in ad) {
                        if (r.call(ad, ag)) {
                            af++
                        }
                    }
                    ac = ad = null;
                    if (!af) {
                        ad = ["valueOf", "toString", "toLocaleString", "propertyIsEnumerable", "isPrototypeOf", "hasOwnProperty", "constructor"];
                        m = function(aj, an) {
                            var am = u.call(aj) == U,
                                al,
                                ak;
                            var ai = !am && typeof aj.constructor != "function" && e[typeof aj.hasOwnProperty] && aj.hasOwnProperty || r;
                            for (al in aj) {
                                if (! (am && al == "prototype") && ai.call(aj, al)) {
                                    an(al)
                                }
                            }
                            for (ak = ad.length; al = ad[--ak]; ai.call(aj, al) && an(al)) {}
                        }
                    } else {
                        if (af == 2) {
                            m = function(aj, am) {
                                var ai = {},
                                    al = u.call(aj) == U,
                                    ak;
                                for (ak in aj) {
                                    if (! (al && ak == "prototype") && !r.call(ai, ak) && (ai[ak] = 1) && r.call(aj, ak)) {
                                        am(ak)
                                    }
                                }
                            }
                        } else {
                            m = function(aj, am) {
                                var al = u.call(aj) == U,
                                    ak,
                                    ai;
                                for (ak in aj) {
                                    if (! (al && ak == "prototype") && r.call(aj, ak) && !(ai = ak === "constructor")) {
                                        am(ak)
                                    }
                                }
                                if (ai || r.call(aj, (ak = "constructor"))) {
                                    am(ak)
                                }
                            }
                        }
                    }
                    return m(ae, ah)
                };
                if (!o("json-stringify")) {
                    var q = {
                        92 : "\\\\",
                        34 : '\\"',
                        8 : "\\b",
                        12 : "\\f",
                        10 : "\\n",
                        13 : "\\r",
                        9 : "\\t"
                    };
                    var I = "000000";
                    var t = function(ac, ad) {
                        return (I + (ad || 0)).slice( - ac)
                    };
                    var z = "\\u00";
                    var C = function(ai) {
                        var ad = '"',
                            ag = 0,
                            ah = ai.length,
                            ac = !F || ah > 10;
                        var af = ac && (F ? ai.split("") : ai);
                        for (; ag < ah; ag++) {
                            var ae = ai.charCodeAt(ag);
                            switch (ae) {
                                case 8:
                                case 9:
                                case 10:
                                case 12:
                                case 13:
                                case 34:
                                case 92:
                                    ad += q[ae];
                                    break;
                                default:
                                    if (ae < 32) {
                                        ad += z + t(2, ae.toString(16));
                                        break
                                    }
                                    ad += ac ? af[ag] : ai.charAt(ag)
                            }
                        }
                        return ad + '"'
                    };
                    var p = function(ai, aA, ag, al, ax, ac, aj) {
                        var at, ae, ap, az, ay, ak, aw, au, aq, an, ar, ad, ah, af, av, ao;
                        try {
                            at = aA[ai]
                        } catch(am) {}
                        if (typeof at == "object" && at) {
                            ae = u.call(at);
                            if (ae == Q && !r.call(at, "toJSON")) {
                                if (at > -1 / 0 && at < 1 / 0) {
                                    if (D) {
                                        ay = s(at / 86400000);
                                        for (ap = s(ay / 365.2425) + 1970 - 1; D(ap + 1, 0) <= ay; ap++) {}
                                        for (az = s((ay - D(ap, 0)) / 30.42); D(ap, az + 1) <= ay; az++) {}
                                        ay = 1 + ay - D(ap, az);
                                        ak = (at % 86400000 + 86400000) % 86400000;
                                        aw = s(ak / 3600000) % 24;
                                        au = s(ak / 60000) % 60;
                                        aq = s(ak / 1000) % 60;
                                        an = ak % 1000
                                    } else {
                                        ap = at.getUTCFullYear();
                                        az = at.getUTCMonth();
                                        ay = at.getUTCDate();
                                        aw = at.getUTCHours();
                                        au = at.getUTCMinutes();
                                        aq = at.getUTCSeconds();
                                        an = at.getUTCMilliseconds()
                                    }
                                    at = (ap <= 0 || ap >= 10000 ? (ap < 0 ? "-": "+") + t(6, ap < 0 ? -ap: ap) : t(4, ap)) + "-" + t(2, az + 1) + "-" + t(2, ay) + "T" + t(2, aw) + ":" + t(2, au) + ":" + t(2, aq) + "." + t(3, an) + "Z"
                                } else {
                                    at = null
                                }
                            } else {
                                if (typeof at.toJSON == "function" && ((ae != N && ae != O && ae != E) || r.call(at, "toJSON"))) {
                                    at = at.toJSON(ai)
                                }
                            }
                        }
                        if (ag) {
                            at = ag.call(aA, ai, at)
                        }
                        if (at === null) {
                            return "null"
                        }
                        ae = u.call(at);
                        if (ae == A) {
                            return "" + at
                        } else {
                            if (ae == N) {
                                return at > -1 / 0 && at < 1 / 0 ? "" + at: "null"
                            } else {
                                if (ae == O) {
                                    return C("" + at)
                                }
                            }
                        }
                        if (typeof at == "object") {
                            for (af = aj.length; af--;) {
                                if (aj[af] === at) {
                                    throw aa()
                                }
                            }
                            aj.push(at);
                            ar = [];
                            av = ac;
                            ac += ax;
                            if (ae == E) {
                                for (ah = 0, af = at.length; ah < af; ah++) {
                                    ad = p(ah, at, ag, al, ax, ac, aj);
                                    ar.push(ad === L ? "null": ad)
                                }
                                ao = ar.length ? (ax ? "[\n" + ac + ar.join(",\n" + ac) + "\n" + av + "]": ("[" + ar.join(",") + "]")) : "[]"
                            } else {
                                m(al || at,
                                    function(aC) {
                                        var aB = p(aC, at, ag, al, ax, ac, aj);
                                        if (aB !== L) {
                                            ar.push(C(aC) + ":" + (ax ? " ": "") + aB)
                                        }
                                    });
                                ao = ar.length ? (ax ? "{\n" + ac + ar.join(",\n" + ac) + "\n" + av + "}": ("{" + ar.join(",") + "}")) : "{}"
                            }
                            aj.pop();
                            return ao
                        }
                    };
                    V.stringify = function(ac, ae, af) {
                        var ad, al, aj, ai;
                        if (e[typeof ae] && ae) {
                            if ((ai = u.call(ae)) == U) {
                                al = ae
                            } else {
                                if (ai == E) {
                                    aj = {};
                                    for (var ah = 0,
                                             ag = ae.length,
                                             ak; ah < ag; ak = ae[ah++], ((ai = u.call(ak)), ai == O || ai == N) && (aj[ak] = 1)) {}
                                }
                            }
                        }
                        if (af) {
                            if ((ai = u.call(af)) == N) {
                                if ((af -= af % 1) > 0) {
                                    for (ad = "", af > 10 && (af = 10); ad.length < af; ad += " ") {}
                                }
                            } else {
                                if (ai == O) {
                                    ad = af.length <= 10 ? af: af.slice(0, 10)
                                }
                            }
                        }
                        return p("", (ak = {},
                            ak[""] = ac, ak), al, aj, ad, "", [])
                    }
                }
                if (!o("json-parse")) {
                    var M = R.fromCharCode;
                    var l = {
                        92 : "\\",
                        34 : '"',
                        47 : "/",
                        98 : "\b",
                        116 : "\t",
                        110 : "\n",
                        102 : "\f",
                        114 : "\r"
                    };
                    var G, X;
                    var H = function() {
                        G = X = null;
                        throw T()
                    };
                    var y = function() {
                        var ah = X,
                            af = ah.length,
                            ag, ae, ac, ai, ad;
                        while (G < af) {
                            ad = ah.charCodeAt(G);
                            switch (ad) {
                                case 9:
                                case 10:
                                case 13:
                                case 32:
                                    G++;
                                    break;
                                case 123:
                                case 125:
                                case 91:
                                case 93:
                                case 58:
                                case 44:
                                    ag = F ? ah.charAt(G) : ah[G];
                                    G++;
                                    return ag;
                                case 34:
                                    for (ag = "@", G++; G < af;) {
                                        ad = ah.charCodeAt(G);
                                        if (ad < 32) {
                                            H()
                                        } else {
                                            if (ad == 92) {
                                                ad = ah.charCodeAt(++G);
                                                switch (ad) {
                                                    case 92:
                                                    case 34:
                                                    case 47:
                                                    case 98:
                                                    case 116:
                                                    case 110:
                                                    case 102:
                                                    case 114:
                                                        ag += l[ad];
                                                        G++;
                                                        break;
                                                    case 117:
                                                        ae = ++G;
                                                        for (ac = G + 4; G < ac; G++) {
                                                            ad = ah.charCodeAt(G);
                                                            if (! (ad >= 48 && ad <= 57 || ad >= 97 && ad <= 102 || ad >= 65 && ad <= 70)) {
                                                                H()
                                                            }
                                                        }
                                                        ag += M("0x" + ah.slice(ae, G));
                                                        break;
                                                    default:
                                                        H()
                                                }
                                            } else {
                                                if (ad == 34) {
                                                    break
                                                }
                                                ad = ah.charCodeAt(G);
                                                ae = G;
                                                while (ad >= 32 && ad != 92 && ad != 34) {
                                                    ad = ah.charCodeAt(++G)
                                                }
                                                ag += ah.slice(ae, G)
                                            }
                                        }
                                    }
                                    if (ah.charCodeAt(G) == 34) {
                                        G++;
                                        return ag
                                    }
                                    H();
                                default:
                                    ae = G;
                                    if (ad == 45) {
                                        ai = true;
                                        ad = ah.charCodeAt(++G)
                                    }
                                    if (ad >= 48 && ad <= 57) {
                                        if (ad == 48 && ((ad = ah.charCodeAt(G + 1)), ad >= 48 && ad <= 57)) {
                                            H()
                                        }
                                        ai = false;
                                        for (; G < af && ((ad = ah.charCodeAt(G)), ad >= 48 && ad <= 57); G++) {}
                                        if (ah.charCodeAt(G) == 46) {
                                            ac = ++G;
                                            for (; ac < af && ((ad = ah.charCodeAt(ac)), ad >= 48 && ad <= 57); ac++) {}
                                            if (ac == G) {
                                                H()
                                            }
                                            G = ac
                                        }
                                        ad = ah.charCodeAt(G);
                                        if (ad == 101 || ad == 69) {
                                            ad = ah.charCodeAt(++G);
                                            if (ad == 43 || ad == 45) {
                                                G++
                                            }
                                            for (ac = G; ac < af && ((ad = ah.charCodeAt(ac)), ad >= 48 && ad <= 57); ac++) {}
                                            if (ac == G) {
                                                H()
                                            }
                                            G = ac
                                        }
                                        return + ah.slice(ae, G)
                                    }
                                    if (ai) {
                                        H()
                                    }
                                    if (ah.slice(G, G + 4) == "true") {
                                        G += 4;
                                        return true
                                    } else {
                                        if (ah.slice(G, G + 5) == "false") {
                                            G += 5;
                                            return false
                                        } else {
                                            if (ah.slice(G, G + 4) == "null") {
                                                G += 4;
                                                return null
                                            }
                                        }
                                    }
                                    H()
                            }
                        }
                        return "$"
                    };
                    var W = function(ad) {
                        var ac, ae;
                        if (ad == "$") {
                            H()
                        }
                        if (typeof ad == "string") {
                            if ((F ? ad.charAt(0) : ad[0]) == "@") {
                                return ad.slice(1)
                            }
                            if (ad == "[") {
                                ac = [];
                                for (;; ae || (ae = true)) {
                                    ad = y();
                                    if (ad == "]") {
                                        break
                                    }
                                    if (ae) {
                                        if (ad == ",") {
                                            ad = y();
                                            if (ad == "]") {
                                                H()
                                            }
                                        } else {
                                            H()
                                        }
                                    }
                                    if (ad == ",") {
                                        H()
                                    }
                                    ac.push(W(ad))
                                }
                                return ac
                            } else {
                                if (ad == "{") {
                                    ac = {};
                                    for (;; ae || (ae = true)) {
                                        ad = y();
                                        if (ad == "}") {
                                            break
                                        }
                                        if (ae) {
                                            if (ad == ",") {
                                                ad = y();
                                                if (ad == "}") {
                                                    H()
                                                }
                                            } else {
                                                H()
                                            }
                                        }
                                        if (ad == "," || typeof ad != "string" || (F ? ad.charAt(0) : ad[0]) != "@" || y() != ":") {
                                            H()
                                        }
                                        ac[ad.slice(1)] = W(y())
                                    }
                                    return ac
                                }
                            }
                            H()
                        }
                        return ad
                    };
                    var P = function(ae, ad, af) {
                        var ac = w(ae, ad, af);
                        if (ac === L) {
                            delete ae[ad]
                        } else {
                            ae[ad] = ac
                        }
                    };
                    var w = function(af, ae, ag) {
                        var ad = af[ae],
                            ac;
                        if (typeof ad == "object" && ad) {
                            if (u.call(ad) == E) {
                                for (ac = ad.length; ac--;) {
                                    P(ad, ac, ag)
                                }
                            } else {
                                m(ad,
                                    function(ah) {
                                        P(ad, ah, ag)
                                    })
                            }
                        }
                        return ag.call(af, ae, ad)
                    };
                    V.parse = function(ae, af) {
                        var ac, ad;
                        G = 0;
                        X = "" + ae;
                        ac = W(y());
                        if (y() != "$") {
                            H()
                        }
                        G = X = null;
                        return af && u.call(af) == U ? w((ad = {},
                            ad[""] = ac, ad), "", af) : ac
                    }
                }
            }
            V.runInContext = j;
            return V
        }
        if (h && !c) {
            j(i, h)
        } else {
            var f = i.JSON,
                k = i.JSON3,
                d = false;
            var g = j(i, (i.JSON3 = {
                noConflict: function() {
                    if (!d) {
                        d = true;
                        i.JSON = f;
                        i.JSON3 = k;
                        f = k = null
                    }
                    return g
                }
            }));
            i.JSON = {
                parse: g.parse,
                stringify: g.stringify
            }
        }
        if (c) {
            define(function() {
                return g
            })
        }
    }).call(this);
    JSON2 = a
})()
}
if (typeof _paq !== "object") {
    _paq = []
}
if (typeof window.Piwik !== "object") {
    window.Piwik = (function() {
        var l, a = {},
            x = document,
            f = navigator,
            N = screen,
            J = window,
            g = J.performance || J.mozPerformance || J.msPerformance || J.webkitPerformance,
            n = J.encodeURIComponent,
            I = J.decodeURIComponent,
            i = unescape,
            O, w, d;
        function k(Y) {
            try {
                return I(Y)
            } catch(Z) {
                return unescape(Y)
            }
        }
        function z(Z) {
            var Y = typeof Z;
            return Y !== "undefined"
        }
        function s(Y) {
            return typeof Y === "function"
        }
        function M(Y) {
            return typeof Y === "object"
        }
        function q(Y) {
            return typeof Y === "string" || Y instanceof String
        }
        function t(Z) {
            if (!Z) {
                return true
            }
            var Y;
            var aa = true;
            for (Y in Z) {
                if (Object.prototype.hasOwnProperty.call(Z, Y)) {
                    aa = false
                }
            }
            return aa
        }
        function T() {
            var Y, aa, Z;
            for (Y = 0; Y < arguments.length; Y += 1) {
                Z = arguments[Y];
                aa = Z.shift();
                if (q(aa)) {
                    O[aa].apply(O, Z)
                } else {
                    aa.apply(O, Z)
                }
            }
        }
        function W(ab, aa, Z, Y) {
            if (ab.addEventListener) {
                ab.addEventListener(aa, Z, Y);
                return true
            }
            if (ab.attachEvent) {
                return ab.attachEvent("on" + aa, Z)
            }
            ab["on" + aa] = Z
        }
        function R(Z, ac) {
            var Y = "",
                ab, aa;
            for (ab in a) {
                if (Object.prototype.hasOwnProperty.call(a, ab)) {
                    aa = a[ab][Z];
                    if (s(aa)) {
                        Y += aa(ac)
                    }
                }
            }
            return Y
        }
        function U() {
            var Y;
            R("unload");
            if (l) {
                do {
                    Y = new Date()
                } while ( Y . getTimeAlias () < l)
            }
        }
        function j(aa, Z) {
            var Y = x.createElement("script");
            Y.type = "text/javascript";
            Y.src = aa;
            if (Y.readyState) {
                Y.onreadystatechange = function() {
                    var ab = this.readyState;
                    if (ab === "loaded" || ab === "complete") {
                        Y.onreadystatechange = null;
                        Z()
                    }
                }
            } else {
                Y.onload = Z
            }
            x.getElementsByTagName("head")[0].appendChild(Y)
        }
        function A() {
            var Y = "";
            try {
                Y = J.top.document.referrer
            } catch(aa) {
                if (J.parent) {
                    try {
                        Y = J.parent.document.referrer
                    } catch(Z) {
                        Y = ""
                    }
                }
            }
            if (Y === "") {
                Y = x.referrer
            }
            return Y
        }
        function m(Y) {
            var aa = new RegExp("^([a-z]+):"),
                Z = aa.exec(Y);
            return Z ? Z[1] : null
        }
        function c(Y) {
            var aa = new RegExp("^(?:(?:https?|ftp):)/*(?:[^@]+@)?([^:/#]+)"),
                Z = aa.exec(Y);
            return Z ? Z[1] : Y
        }
        function L(aa, Z) {
            var Y = "[\\?&#]" + Z + "=([^&#]*)";
            var ac = new RegExp(Y);
            var ab = ac.exec(aa);
            return ab ? I(ab[1]) : ""
        }
        function v(Y) {
            return unescape(n(Y))
        }
        function V(an) {
            var aa = function(au, at) {
                    return (au << at) | (au >>> (32 - at))
                },
                ao = function(aw) {
                    var au = "",
                        av, at;
                    for (av = 7; av >= 0; av--) {
                        at = (aw >>> (av * 4)) & 15;
                        au += at.toString(16)
                    }
                    return au
                },
                ad,
                aq,
                ap,
                Z = [],
                ah = 1732584193,
                af = 4023233417,
                ae = 2562383102,
                ac = 271733878,
                ab = 3285377520,
                am,
                al,
                ak,
                aj,
                ai,
                ar,
                Y,
                ag = [];
            an = v(an);
            Y = an.length;
            for (aq = 0; aq < Y - 3; aq += 4) {
                ap = an.charCodeAt(aq) << 24 | an.charCodeAt(aq + 1) << 16 | an.charCodeAt(aq + 2) << 8 | an.charCodeAt(aq + 3);
                ag.push(ap)
            }
            switch (Y & 3) {
                case 0:
                    aq = 2147483648;
                    break;
                case 1:
                    aq = an.charCodeAt(Y - 1) << 24 | 8388608;
                    break;
                case 2:
                    aq = an.charCodeAt(Y - 2) << 24 | an.charCodeAt(Y - 1) << 16 | 32768;
                    break;
                case 3:
                    aq = an.charCodeAt(Y - 3) << 24 | an.charCodeAt(Y - 2) << 16 | an.charCodeAt(Y - 1) << 8 | 128;
                    break
            }
            ag.push(aq);
            while ((ag.length & 15) !== 14) {
                ag.push(0)
            }
            ag.push(Y >>> 29);
            ag.push((Y << 3) & 4294967295);
            for (ad = 0; ad < ag.length; ad += 16) {
                for (aq = 0; aq < 16; aq++) {
                    Z[aq] = ag[ad + aq]
                }
                for (aq = 16; aq <= 79; aq++) {
                    Z[aq] = aa(Z[aq - 3] ^ Z[aq - 8] ^ Z[aq - 14] ^ Z[aq - 16], 1)
                }
                am = ah;
                al = af;
                ak = ae;
                aj = ac;
                ai = ab;
                for (aq = 0; aq <= 19; aq++) {
                    ar = (aa(am, 5) + ((al & ak) | (~al & aj)) + ai + Z[aq] + 1518500249) & 4294967295;
                    ai = aj;
                    aj = ak;
                    ak = aa(al, 30);
                    al = am;
                    am = ar
                }
                for (aq = 20; aq <= 39; aq++) {
                    ar = (aa(am, 5) + (al ^ ak ^ aj) + ai + Z[aq] + 1859775393) & 4294967295;
                    ai = aj;
                    aj = ak;
                    ak = aa(al, 30);
                    al = am;
                    am = ar
                }
                for (aq = 40; aq <= 59; aq++) {
                    ar = (aa(am, 5) + ((al & ak) | (al & aj) | (ak & aj)) + ai + Z[aq] + 2400959708) & 4294967295;
                    ai = aj;
                    aj = ak;
                    ak = aa(al, 30);
                    al = am;
                    am = ar
                }
                for (aq = 60; aq <= 79; aq++) {
                    ar = (aa(am, 5) + (al ^ ak ^ aj) + ai + Z[aq] + 3395469782) & 4294967295;
                    ai = aj;
                    aj = ak;
                    ak = aa(al, 30);
                    al = am;
                    am = ar
                }
                ah = (ah + am) & 4294967295;
                af = (af + al) & 4294967295;
                ae = (ae + ak) & 4294967295;
                ac = (ac + aj) & 4294967295;
                ab = (ab + ai) & 4294967295
            }
            ar = ao(ah) + ao(af) + ao(ae) + ao(ac) + ao(ab);
            return ar.toLowerCase()
        }
        function Q(aa, Y, Z) {
            if (!aa) {
                aa = ""
            }
            if (!Y) {
                Y = ""
            }
            if (aa === "translate.googleusercontent.com") {
                if (Z === "") {
                    Z = Y
                }
                Y = L(Y, "u");
                aa = c(Y)
            } else {
                if (aa === "cc.bingj.com" || aa === "webcache.googleusercontent.com" || aa.slice(0, 5) === "74.6.") {
                    Y = x.links[0].href;
                    aa = c(Y)
                }
            }
            return [aa, Y, Z]
        }
        function B(Z) {
            var Y = Z.length;
            if (Z.charAt(--Y) === ".") {
                Z = Z.slice(0, Y)
            }
            if (Z.slice(0, 2) === "*.") {
                Z = Z.slice(1)
            }
            if (Z.indexOf("/") !== -1) {
                Z = Z.substr(0, Z.indexOf("/"))
            }
            return Z
        }
        function X(Z) {
            Z = Z && Z.text ? Z.text: Z;
            if (!q(Z)) {
                var Y = x.getElementsByTagName("title");
                if (Y && z(Y[0])) {
                    Z = Y[0].text
                }
            }
            return Z
        }
        function F(Y) {
            if (!Y) {
                return []
            }
            if (!z(Y.children) && z(Y.childNodes)) {
                return Y.children
            }
            if (z(Y.children)) {
                return Y.children
            }
            return []
        }
        function K(Z, Y) {
            if (!Z || !Y) {
                return false
            }
            if (Z.contains) {
                return Z.contains(Y)
            }
            if (Z === Y) {
                return true
            }
            if (Z.compareDocumentPosition) {
                return !! (Z.compareDocumentPosition(Y) & 16)
            }
            return false
        }
        function C(aa, ab) {
            if (aa && aa.indexOf) {
                return aa.indexOf(ab)
            }
            if (!z(aa) || aa === null) {
                return - 1
            }
            if (!aa.length) {
                return - 1
            }
            var Y = aa.length;
            if (Y === 0) {
                return - 1
            }
            var Z = 0;
            while (Z < Y) {
                if (aa[Z] === ab) {
                    return Z
                }
                Z++
            }
            return - 1
        }
        function H(Z, Y) {
            Z = String(Z);
            return Z.indexOf(Y, Z.length - Y.length) !== -1
        }
        function r(Z, Y) {
            Z = String(Z);
            return Z.indexOf(Y) !== -1
        }
        function e(Z, Y) {
            Z = String(Z);
            return Z.substr(0, Z.length - Y)
        }
        function h(aa) {
            if (!aa) {
                return false
            }
            function Y(ac, ad) {
                if (J.getComputedStyle) {
                    return x.defaultView.getComputedStyle(ac, null)[ad]
                }
                if (ac.currentStyle) {
                    return ac.currentStyle[ad]
                }
            }
            function ab(ac) {
                ac = ac.parentNode;
                while (ac) {
                    if (ac === x) {
                        return true
                    }
                    ac = ac.parentNode
                }
                return false
            }
            function Z(ae, ak, ac, ah, af, ai, ag) {
                var ad = ae.parentNode,
                    aj = 1;
                if (!ab(ae)) {
                    return false
                }
                if (9 === ad.nodeType) {
                    return true
                }
                if ("0" === Y(ae, "opacity") || "none" === Y(ae, "display") || "hidden" === Y(ae, "visibility")) {
                    return false
                }
                if (!z(ak) || !z(ac) || !z(ah) || !z(af) || !z(ai) || !z(ag)) {
                    ak = ae.offsetTop;
                    af = ae.offsetLeft;
                    ah = ak + ae.offsetHeight;
                    ac = af + ae.offsetWidth;
                    ai = ae.offsetWidth;
                    ag = ae.offsetHeight
                }
                if (aa === ae && (0 === ag || 0 === ai) && "hidden" === Y(ae, "overflow")) {
                    return false
                }
                if (ad) {
                    if (("hidden" === Y(ad, "overflow") || "scroll" === Y(ad, "overflow"))) {
                        if (af + aj > ad.offsetWidth + ad.scrollLeft || af + ai - aj < ad.scrollLeft || ak + aj > ad.offsetHeight + ad.scrollTop || ak + ag - aj < ad.scrollTop) {
                            return false
                        }
                    }
                    if (ae.offsetParent === ad) {
                        af += ad.offsetLeft;
                        ak += ad.offsetTop
                    }
                    return Z(ad, ak, ac, ah, af, ai, ag)
                }
                return true
            }
            return Z(aa)
        }
        var S = {
            htmlCollectionToArray: function(aa) {
                var Y = [],
                    Z;
                if (!aa || !aa.length) {
                    return Y
                }
                for (Z = 0; Z < aa.length; Z++) {
                    Y.push(aa[Z])
                }
                return Y
            },
            find: function(Y) {
                if (!document.querySelectorAll || !Y) {
                    return []
                }
                var Z = document.querySelectorAll(Y);
                return this.htmlCollectionToArray(Z)
            },
            findMultiple: function(aa) {
                if (!aa || !aa.length) {
                    return []
                }
                var Z, ab;
                var Y = [];
                for (Z = 0; Z < aa.length; Z++) {
                    ab = this.find(aa[Z]);
                    Y = Y.concat(ab)
                }
                Y = this.makeNodesUnique(Y);
                return Y
            },
            findNodesByTagName: function(Z, Y) {
                if (!Z || !Y || !Z.getElementsByTagName) {
                    return []
                }
                var aa = Z.getElementsByTagName(Y);
                return this.htmlCollectionToArray(aa)
            },
            makeNodesUnique: function(Y) {
                var ad = [].concat(Y);
                Y.sort(function(af, ae) {
                    if (af === ae) {
                        return 0
                    }
                    var ah = C(ad, af);
                    var ag = C(ad, ae);
                    if (ah === ag) {
                        return 0
                    }
                    return ah > ag ? -1 : 1
                });
                if (Y.length <= 1) {
                    return Y
                }
                var Z = 0;
                var ab = 0;
                var ac = [];
                var aa;
                aa = Y[Z++];
                while (aa) {
                    if (aa === Y[Z]) {
                        ab = ac.push(Z)
                    }
                    aa = Y[Z++] || null
                }
                while (ab--) {
                    Y.splice(ac[ab], 1)
                }
                return Y
            },
            getAttributeValueFromNode: function(ac, aa) {
                if (!this.hasNodeAttribute(ac, aa)) {
                    return
                }
                if (ac && ac.getAttribute) {
                    return ac.getAttribute(aa)
                }
                if (!ac || !ac.attributes) {
                    return
                }
                var ab = (typeof ac.attributes[aa]);
                if ("undefined" === ab) {
                    return
                }
                if (ac.attributes[aa].value) {
                    return ac.attributes[aa].value
                }
                if (ac.attributes[aa].nodeValue) {
                    return ac.attributes[aa].nodeValue
                }
                var Z;
                var Y = ac.attributes;
                if (!Y) {
                    return
                }
                for (Z = 0; Z < Y.length; Z++) {
                    if (Y[Z].nodeName === aa) {
                        return Y[Z].nodeValue
                    }
                }
                return null
            },
            hasNodeAttributeWithValue: function(Z, Y) {
                var aa = this.getAttributeValueFromNode(Z, Y);
                return !! aa
            },
            hasNodeAttribute: function(aa, Y) {
                if (aa && aa.hasAttribute) {
                    return aa.hasAttribute(Y)
                }
                if (aa && aa.attributes) {
                    var Z = (typeof aa.attributes[Y]);
                    return "undefined" !== Z
                }
                return false
            },
            hasNodeCssClass: function(aa, Y) {
                if (aa && Y && aa.className) {
                    var Z = typeof aa.className === "string" ? aa.className.split(" ") : [];
                    if ( - 1 !== C(Z, Y)) {
                        return true
                    }
                }
                return false
            },
            findNodesHavingAttribute: function(ac, aa, Y) {
                if (!Y) {
                    Y = []
                }
                if (!ac || !aa) {
                    return Y
                }
                var ab = F(ac);
                if (!ab || !ab.length) {
                    return Y
                }
                var Z, ad;
                for (Z = 0; Z < ab.length; Z++) {
                    ad = ab[Z];
                    if (this.hasNodeAttribute(ad, aa)) {
                        Y.push(ad)
                    }
                    Y = this.findNodesHavingAttribute(ad, aa, Y)
                }
                return Y
            },
            findFirstNodeHavingAttribute: function(aa, Z) {
                if (!aa || !Z) {
                    return
                }
                if (this.hasNodeAttribute(aa, Z)) {
                    return aa
                }
                var Y = this.findNodesHavingAttribute(aa, Z);
                if (Y && Y.length) {
                    return Y[0]
                }
            },
            findFirstNodeHavingAttributeWithValue: function(ab, aa) {
                if (!ab || !aa) {
                    return
                }
                if (this.hasNodeAttributeWithValue(ab, aa)) {
                    return ab
                }
                var Y = this.findNodesHavingAttribute(ab, aa);
                if (!Y || !Y.length) {
                    return
                }
                var Z;
                for (Z = 0; Z < Y.length; Z++) {
                    if (this.getAttributeValueFromNode(Y[Z], aa)) {
                        return Y[Z]
                    }
                }
            },
            findNodesHavingCssClass: function(ac, ab, Y) {
                if (!Y) {
                    Y = []
                }
                if (!ac || !ab) {
                    return Y
                }
                if (ac.getElementsByClassName) {
                    var ad = ac.getElementsByClassName(ab);
                    return this.htmlCollectionToArray(ad)
                }
                var aa = F(ac);
                if (!aa || !aa.length) {
                    return []
                }
                var Z, ae;
                for (Z = 0; Z < aa.length; Z++) {
                    ae = aa[Z];
                    if (this.hasNodeCssClass(ae, ab)) {
                        Y.push(ae)
                    }
                    Y = this.findNodesHavingCssClass(ae, ab, Y)
                }
                return Y
            },
            findFirstNodeHavingClass: function(aa, Z) {
                if (!aa || !Z) {
                    return
                }
                if (this.hasNodeCssClass(aa, Z)) {
                    return aa
                }
                var Y = this.findNodesHavingCssClass(aa, Z);
                if (Y && Y.length) {
                    return Y[0]
                }
            },
            isLinkElement: function(Z) {
                if (!Z) {
                    return false
                }
                var Y = String(Z.nodeName).toLowerCase();
                var ab = ["a", "area"];
                var aa = C(ab, Y);
                return aa !== -1
            },
            setAnyAttribute: function(Z, Y, aa) {
                if (!Z || !Y) {
                    return
                }
                if (Z.setAttribute) {
                    Z.setAttribute(Y, aa)
                } else {
                    Z[Y] = aa
                }
            }
        };
        var p = {
            CONTENT_ATTR: "data-track-content",
            CONTENT_CLASS: "piwikTrackContent",
            CONTENT_NAME_ATTR: "data-content-name",
            CONTENT_PIECE_ATTR: "data-content-piece",
            CONTENT_PIECE_CLASS: "piwikContentPiece",
            CONTENT_TARGET_ATTR: "data-content-target",
            CONTENT_TARGET_CLASS: "piwikContentTarget",
            CONTENT_IGNOREINTERACTION_ATTR: "data-content-ignoreinteraction",
            CONTENT_IGNOREINTERACTION_CLASS: "piwikContentIgnoreInteraction",
            location: undefined,
            findContentNodes: function() {
                var Z = "." + this.CONTENT_CLASS;
                var Y = "[" + this.CONTENT_ATTR + "]";
                var aa = S.findMultiple([Z, Y]);
                return aa
            },
            findContentNodesWithinNode: function(ab) {
                if (!ab) {
                    return []
                }
                var Z = S.findNodesHavingCssClass(ab, this.CONTENT_CLASS);
                var Y = S.findNodesHavingAttribute(ab, this.CONTENT_ATTR);
                if (Y && Y.length) {
                    var aa;
                    for (aa = 0; aa < Y.length; aa++) {
                        Z.push(Y[aa])
                    }
                }
                if (S.hasNodeAttribute(ab, this.CONTENT_ATTR)) {
                    Z.push(ab)
                } else {
                    if (S.hasNodeCssClass(ab, this.CONTENT_CLASS)) {
                        Z.push(ab)
                    }
                }
                Z = S.makeNodesUnique(Z);
                return Z
            },
            findParentContentNode: function(Z) {
                if (!Z) {
                    return
                }
                var aa = Z;
                var Y = 0;
                while (aa && aa !== x && aa.parentNode) {
                    if (S.hasNodeAttribute(aa, this.CONTENT_ATTR)) {
                        return aa
                    }
                    if (S.hasNodeCssClass(aa, this.CONTENT_CLASS)) {
                        return aa
                    }
                    aa = aa.parentNode;
                    if (Y > 1000) {
                        break
                    }
                    Y++
                }
            },
            findPieceNode: function(Z) {
                var Y;
                Y = S.findFirstNodeHavingAttribute(Z, this.CONTENT_PIECE_ATTR);
                if (!Y) {
                    Y = S.findFirstNodeHavingClass(Z, this.CONTENT_PIECE_CLASS)
                }
                if (Y) {
                    return Y
                }
                return Z
            },
            findTargetNodeNoDefault: function(Y) {
                if (!Y) {
                    return
                }
                var Z = S.findFirstNodeHavingAttributeWithValue(Y, this.CONTENT_TARGET_ATTR);
                if (Z) {
                    return Z
                }
                Z = S.findFirstNodeHavingAttribute(Y, this.CONTENT_TARGET_ATTR);
                if (Z) {
                    return Z
                }
                Z = S.findFirstNodeHavingClass(Y, this.CONTENT_TARGET_CLASS);
                if (Z) {
                    return Z
                }
            },
            findTargetNode: function(Y) {
                var Z = this.findTargetNodeNoDefault(Y);
                if (Z) {
                    return Z
                }
                return Y
            },
            findContentName: function(Z) {
                if (!Z) {
                    return
                }
                var ac = S.findFirstNodeHavingAttributeWithValue(Z, this.CONTENT_NAME_ATTR);
                if (ac) {
                    return S.getAttributeValueFromNode(ac, this.CONTENT_NAME_ATTR)
                }
                var Y = this.findContentPiece(Z);
                if (Y) {
                    return this.removeDomainIfIsInLink(Y)
                }
                if (S.hasNodeAttributeWithValue(Z, "title")) {
                    return S.getAttributeValueFromNode(Z, "title")
                }
                var aa = this.findPieceNode(Z);
                if (S.hasNodeAttributeWithValue(aa, "title")) {
                    return S.getAttributeValueFromNode(aa, "title")
                }
                var ab = this.findTargetNode(Z);
                if (S.hasNodeAttributeWithValue(ab, "title")) {
                    return S.getAttributeValueFromNode(ab, "title")
                }
            },
            findContentPiece: function(Z) {
                if (!Z) {
                    return
                }
                var ab = S.findFirstNodeHavingAttributeWithValue(Z, this.CONTENT_PIECE_ATTR);
                if (ab) {
                    return S.getAttributeValueFromNode(ab, this.CONTENT_PIECE_ATTR)
                }
                var Y = this.findPieceNode(Z);
                var aa = this.findMediaUrlInNode(Y);
                if (aa) {
                    return this.toAbsoluteUrl(aa)
                }
            },
            findContentTarget: function(aa) {
                if (!aa) {
                    return
                }
                var ab = this.findTargetNode(aa);
                if (S.hasNodeAttributeWithValue(ab, this.CONTENT_TARGET_ATTR)) {
                    return S.getAttributeValueFromNode(ab, this.CONTENT_TARGET_ATTR)
                }
                var Z;
                if (S.hasNodeAttributeWithValue(ab, "href")) {
                    Z = S.getAttributeValueFromNode(ab, "href");
                    return this.toAbsoluteUrl(Z)
                }
                var Y = this.findPieceNode(aa);
                if (S.hasNodeAttributeWithValue(Y, "href")) {
                    Z = S.getAttributeValueFromNode(Y, "href");
                    return this.toAbsoluteUrl(Z)
                }
            },
            isSameDomain: function(Y) {
                if (!Y || !Y.indexOf) {
                    return false
                }
                if (0 === Y.indexOf(this.getLocation().origin)) {
                    return true
                }
                var Z = Y.indexOf(this.getLocation().host);
                if (8 >= Z && 0 <= Z) {
                    return true
                }
                return false
            },
            removeDomainIfIsInLink: function(aa) {
                var Z = "^https?://[^/]+";
                var Y = "^.*//[^/]+";
                if (aa && aa.search && -1 !== aa.search(new RegExp(Z)) && this.isSameDomain(aa)) {
                    aa = aa.replace(new RegExp(Y), "");
                    if (!aa) {
                        aa = "/"
                    }
                }
                return aa
            },
            findMediaUrlInNode: function(ac) {
                if (!ac) {
                    return
                }
                var aa = ["img", "embed", "video", "audio"];
                var Y = ac.nodeName.toLowerCase();
                if ( - 1 !== C(aa, Y) && S.findFirstNodeHavingAttributeWithValue(ac, "src")) {
                    var ab = S.findFirstNodeHavingAttributeWithValue(ac, "src");
                    return S.getAttributeValueFromNode(ab, "src")
                }
                if (Y === "object" && S.hasNodeAttributeWithValue(ac, "data")) {
                    return S.getAttributeValueFromNode(ac, "data")
                }
                if (Y === "object") {
                    var ad = S.findNodesByTagName(ac, "param");
                    if (ad && ad.length) {
                        var Z;
                        for (Z = 0; Z < ad.length; Z++) {
                            if ("movie" === S.getAttributeValueFromNode(ad[Z], "name") && S.hasNodeAttributeWithValue(ad[Z], "value")) {
                                return S.getAttributeValueFromNode(ad[Z], "value")
                            }
                        }
                    }
                    var ae = S.findNodesByTagName(ac, "embed");
                    if (ae && ae.length) {
                        return this.findMediaUrlInNode(ae[0])
                    }
                }
            },
            trim: function(Y) {
                if (Y && String(Y) === Y) {
                    return Y.replace(/^\s+|\s+$/g, "")
                }
                return Y
            },
            isOrWasNodeInViewport: function(ad) {
                if (!ad || !ad.getBoundingClientRect || ad.nodeType !== 1) {
                    return true
                }
                var ac = ad.getBoundingClientRect();
                var ab = x.documentElement || {};
                var aa = ac.top < 0;
                if (aa && ad.offsetTop) {
                    aa = (ad.offsetTop + ac.height) > 0
                }
                var Z = ab.clientWidth;
                if (J.innerWidth && Z > J.innerWidth) {
                    Z = J.innerWidth
                }
                var Y = ab.clientHeight;
                if (J.innerHeight && Y > J.innerHeight) {
                    Y = J.innerHeight
                }
                return ((ac.bottom > 0 || aa) && ac.right > 0 && ac.left < Z && ((ac.top < Y) || aa))
            },
            isNodeVisible: function(Z) {
                var Y = h(Z);
                var aa = this.isOrWasNodeInViewport(Z);
                return Y && aa
            },
            buildInteractionRequestParams: function(Y, Z, aa, ab) {
                var ac = "";
                if (Y) {
                    ac += "c_i=" + n(Y)
                }
                if (Z) {
                    if (ac) {
                        ac += "&"
                    }
                    ac += "c_n=" + n(Z)
                }
                if (aa) {
                    if (ac) {
                        ac += "&"
                    }
                    ac += "c_p=" + n(aa)
                }
                if (ab) {
                    if (ac) {
                        ac += "&"
                    }
                    ac += "c_t=" + n(ab)
                }
                return ac
            },
            buildImpressionRequestParams: function(Y, Z, aa) {
                var ab = "c_n=" + n(Y) + "&c_p=" + n(Z);
                if (aa) {
                    ab += "&c_t=" + n(aa)
                }
                return ab
            },
            buildContentBlock: function(aa) {
                if (!aa) {
                    return
                }
                var Y = this.findContentName(aa);
                var Z = this.findContentPiece(aa);
                var ab = this.findContentTarget(aa);
                Y = this.trim(Y);
                Z = this.trim(Z);
                ab = this.trim(ab);
                return {
                    name: Y || "Unknown",
                    piece: Z || "Unknown",
                    target: ab || ""
                }
            },
            collectContent: function(ab) {
                if (!ab || !ab.length) {
                    return []
                }
                var aa = [];
                var Y, Z;
                for (Y = 0; Y < ab.length; Y++) {
                    Z = this.buildContentBlock(ab[Y]);
                    if (z(Z)) {
                        aa.push(Z)
                    }
                }
                return aa
            },
            setLocation: function(Y) {
                this.location = Y
            },
            getLocation: function() {
                var Y = this.location || J.location;
                if (!Y.origin) {
                    Y.origin = Y.protocol + "//" + Y.hostname + (Y.port ? ":" + Y.port: "")
                }
                return Y
            },
            toAbsoluteUrl: function(Z) {
                if ((!Z || String(Z) !== Z) && Z !== "") {
                    return Z
                }
                if ("" === Z) {
                    return this.getLocation().href
                }
                if (Z.search(/^\/\//) !== -1) {
                    return this.getLocation().protocol + Z
                }
                if (Z.search(/:\/\//) !== -1) {
                    return Z
                }
                if (0 === Z.indexOf("#")) {
                    return this.getLocation().origin + this.getLocation().pathname + Z
                }
                if (0 === Z.indexOf("?")) {
                    return this.getLocation().origin + this.getLocation().pathname + Z
                }
                if (0 === Z.search("^[a-zA-Z]{2,11}:")) {
                    return Z
                }
                if (Z.search(/^\//) !== -1) {
                    return this.getLocation().origin + Z
                }
                var Y = "(.*/)";
                var aa = this.getLocation().origin + this.getLocation().pathname.match(new RegExp(Y))[0];
                return aa + Z
            },
            isUrlToCurrentDomain: function(Z) {
                var aa = this.toAbsoluteUrl(Z);
                if (!aa) {
                    return false
                }
                var Y = this.getLocation().origin;
                if (Y === aa) {
                    return true
                }
                if (0 === String(aa).indexOf(Y)) {
                    if (":" === String(aa).substr(Y.length, 1)) {
                        return false
                    }
                    return true
                }
                return false
            },
            setHrefAttribute: function(Z, Y) {
                if (!Z || !Y) {
                    return
                }
                S.setAnyAttribute(Z, "href", Y)
            },
            shouldIgnoreInteraction: function(aa) {
                var Z = S.hasNodeAttribute(aa, this.CONTENT_IGNOREINTERACTION_ATTR);
                var Y = S.hasNodeCssClass(aa, this.CONTENT_IGNOREINTERACTION_CLASS);
                return Z || Y
            }
        };
        function E(Z, ac) {
            if (ac) {
                return ac
            }
            if (r(Z, "?")) {
                var ab = Z.indexOf("?");
                Z = Z.slice(0, ab)
            }
            if (H(Z, "piwik.php")) {
                Z = e(Z, "piwik.php".length)
            } else {
                if (H(Z, ".php")) {
                    var Y = Z.lastIndexOf("/");
                    var aa = 1;
                    Z = Z.slice(0, Y + aa)
                }
            }
            if (H(Z, "/js/")) {
                Z = e(Z, "js/".length)
            }
            return Z
        }
        function D(ae) {
            var ag = "Piwik_Overlay";
            var Z = new RegExp("index\\.php\\?module=Overlay&action=startOverlaySession&idSite=([0-9]+)&period=([^&]+)&date=([^&]+)(&segment=.*)?$");
            var aa = Z.exec(x.referrer);
            if (aa) {
                var ac = aa[1];
                if (ac !== String(ae)) {
                    return false
                }
                var ad = aa[2],
                    Y = aa[3],
                    ab = aa[4];
                if (!ab) {
                    ab = ""
                } else {
                    if (ab.indexOf("&segment=") === 0) {
                        ab = ab.substr("&segment=".length)
                    }
                }
                J.name = ag + "###" + ad + "###" + Y + "###" + ab
            }
            var af = J.name.split("###");
            return af.length === 4 && af[0] === ag
        }
        function P(Z, af, ab) {
            var ae = J.name.split("###"),
                ad = ae[1],
                Y = ae[2],
                ac = ae[3],
                aa = E(Z, af);
            j(aa + "plugins/Overlay/client/client.js?v=1",
                function() {
                    Piwik_Overlay_Client.initialize(aa, ab, ad, Y, ac)
                })
        }
        function o() {
            if (z(J.frameElement)) {
                return (J.frameElement && String(J.frameElement.nodeName).toLowerCase() === "iframe")
            }
            try {
                return J.self !== J.top
            } catch(Y) {
                return true
            }
        }
        function G(bG, bA) {
            var bw = Q(x.domain, J.location.href, A()),
                ce = B(bw[0]),
                bg = k(bw[1]),
                aV = k(bw[2]),
                cc = false,
                bK = "GET",
                cq = bK,
                am = "application/x-www-form-urlencoded; charset=UTF-8",
                bW = am,
                ai = bG || "",
                bb = "",
                ci = "",
                by = bA || "",
                a4 = "",
                bh = "",
                aG,
                aR = x.title,
                cn = ["7z", "aac", "apk", "arc", "arj", "asf", "asx", "avi", "azw3", "bin", "csv", "deb", "dmg", "doc", "docx", "epub", "exe", "flv", "gif", "gz", "gzip", "hqx", "ibooks", "jar", "jpg", "jpeg", "js", "mobi", "mp2", "mp3", "mp4", "mpg", "mpeg", "mov", "movie", "msi", "msp", "odb", "odf", "odg", "ods", "odt", "ogg", "ogv", "pdf", "phps", "png", "ppt", "pptx", "qt", "qtm", "ra", "ram", "rar", "rpm", "sea", "sit", "tar", "tbz", "tbz2", "bz", "bz2", "tgz", "torrent", "txt", "wav", "wma", "wmv", "wpd", "xls", "xlsx", "xml", "z", "zip"],
                ae = [ce],
                a5 = [],
                be = [],
                aJ = [],
                bc = 500,
                b5,
                aH,
                bk,
                bi,
                Y,
                bS = ["pk_campaign", "piwik_campaign", "utm_campaign", "utm_source", "utm_medium"],
                ba = ["pk_kwd", "piwik_kwd", "utm_term"],
                aS = "_pk_",
                cg,
                aX,
                aT = false,
                ca,
                aP,
                a1,
                b6 = 33955200000,
                bQ = 1800000,
                cm = 15768000000,
                aE = true,
                bO = 0,
                bj = false,
                at = false,
                bD,
                bo = {},
                bN = {},
                aU = {},
                a0 = 200,
                cj = {},
                co = {},
                bC = [],
                bH = false,
                bZ = false,
                Z = false,
                cp = false,
                aq = false,
                aO = o(),
                ch = null,
                bE,
                au,
                a6,
                bz = V,
                aW;
            function ct(cD, cA, cz, cC, cy, cB) {
                if (aT) {
                    return
                }
                var cx;
                if (cz) {
                    cx = new Date();
                    cx.setTime(cx.getTime() + cz)
                }
                x.cookie = cD + "=" + n(cA) + (cz ? ";expires=" + cx.toGMTString() : "") + ";path=" + (cC || "/") + (cy ? ";domain=" + cy: "") + (cB ? ";secure": "")
            }
            function ah(cz) {
                if (aT) {
                    return 0
                }
                var cx = new RegExp("(^|;)[ ]*" + cz + "=([^;]*)"),
                    cy = cx.exec(x.cookie);
                return cy ? I(cy[2]) : 0
            }
            function bu(cx) {
                var cy;
                if (bi) {
                    cy = new RegExp("#.*");
                    return cx.replace(cy, "")
                }
                return cx
            }
            function bn(cz, cx) {
                var cA = m(cx),
                    cy;
                if (cA) {
                    return cx
                }
                if (cx.slice(0, 1) === "/") {
                    return m(cz) + "://" + c(cz) + cx
                }
                cz = bu(cz);
                cy = cz.indexOf("?");
                if (cy >= 0) {
                    cz = cz.slice(0, cy)
                }
                cy = cz.lastIndexOf("/");
                if (cy !== cz.length - 1) {
                    cz = cz.slice(0, cy + 1)
                }
                return cz + cx
            }
            function b3(cz, cx) {
                var cy;
                cz = String(cz).toLowerCase();
                cx = String(cx).toLowerCase();
                if (cz === cx) {
                    return true
                }
                if (cx.slice(0, 1) === ".") {
                    if (cz === cx.slice(1)) {
                        return true
                    }
                    cy = cz.length - cx.length;
                    if ((cy > 0) && (cz.slice(cy) === cx)) {
                        return true
                    }
                }
                return false
            }
            function bM(cx) {
                var cy = document.createElement("a");
                if (cx.indexOf("//") !== 0 && cx.indexOf("http") !== 0) {
                    cx = "http://" + cx
                }
                cy.href = p.toAbsoluteUrl(cx);
                if (cy.pathname) {
                    return cy.pathname
                }
                return ""
            }
            function aF(cy, cx) {
                var cz = (!cx || cx === "/" || cx === "/*");
                if (cz) {
                    return true
                }
                if (cy === cx) {
                    return true
                }
                if (!cy) {
                    return false
                }
                cx = String(cx).toLowerCase();
                cy = String(cy).toLowerCase();
                if (H(cx, "*")) {
                    cx = cx.slice(0, -1);
                    cz = (!cx || cx === "/");
                    if (cz) {
                        return true
                    }
                    if (cy === cx) {
                        return true
                    }
                    return cy.indexOf(cx) === 0
                }
                if (!H(cy, "/")) {
                    cy += "/"
                }
                if (!H(cx, "/")) {
                    cx += "/"
                }
                return cy.indexOf(cx) === 0
            }
            function ab(cB, cD) {
                var cy, cx, cz, cA, cC;
                for (cy = 0; cy < ae.length; cy++) {
                    cA = B(ae[cy]);
                    cC = bM(ae[cy]);
                    if (b3(cB, cA) && aF(cD, cC)) {
                        return true
                    }
                }
                return false
            }
            function ay(cA) {
                var cy, cx, cz;
                for (cy = 0; cy < ae.length; cy++) {
                    cx = B(ae[cy].toLowerCase());
                    if (cA === cx) {
                        return true
                    }
                    if (cx.slice(0, 1) === ".") {
                        if (cA === cx.slice(1)) {
                            return true
                        }
                        cz = cA.length - cx.length;
                        if ((cz > 0) && (cA.slice(cz) === cx)) {
                            return true
                        }
                    }
                }
                return false
            }
            function bR(cx, cz) {
                var cy = new Image(1, 1);
                cy.onload = function() {
                    w = 0;
                    if (typeof cz === "function") {
                        cz()
                    }
                };
                cy.src = ai + (ai.indexOf("?") < 0 ? "?": "&") + cx
            }
            function cl(cy, cB, cx) {
                if (!z(cx) || null === cx) {
                    cx = true
                }
                try {
                    var cA = J.XMLHttpRequest ? new J.XMLHttpRequest() : J.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : null;
                    cA.open("POST", ai, true);
                    cA.onreadystatechange = function() {
                        if (this.readyState === 4 && !(this.status >= 200 && this.status < 300) && cx) {
                            bR(cy, cB)
                        } else {
                            if (typeof cB === "function") {
                                cB()
                            }
                        }
                    };
                    cA.setRequestHeader("Content-Type", bW);
                    cA.send(cy)
                } catch(cz) {
                    if (cx) {
                        bR(cy, cB)
                    }
                }
            }
            function bI(cy) {
                var cx = new Date();
                var cz = cx.getTime() + cy;
                if (!l || cz > l) {
                    l = cz
                }
            }
            function bP(cx) {
                if (bE || !aH) {
                    return
                }
                bE = setTimeout(function cy() {
                        bE = null;
                        if (!aO) {
                            aO = (!x.hasFocus || x.hasFocus())
                        }
                        if (!aO) {
                            bP(aH);
                            return
                        }
                        if (bk()) {
                            return
                        }
                        var cz = new Date(),
                            cA = aH - (cz.getTime() - ch);
                        cA = Math.min(aH, cA);
                        bP(cA)
                    },
                    cx || aH)
            }
            function bd() {
                if (!bE) {
                    return
                }
                clearTimeout(bE);
                bE = null
            }
            function aL() {
                aO = true;
                if (bk()) {
                    return
                }
                bP()
            }
            function af() {
                bd()
            }
            function cv() {
                if (aq || !aH) {
                    return
                }
                aq = true;
                W(J, "focus", aL);
                W(J, "blur", af);
                bP()
            }
            function b0(cB) {
                var cy = new Date();
                var cx = cy.getTime();
                ch = cx;
                if (bZ && cx < bZ) {
                    var cz = bZ - cx;
                    setTimeout(cB, cz);
                    bI(cz + 50);
                    bZ += 50;
                    return
                }
                if (bZ === false) {
                    var cA = 800;
                    bZ = cx + cA
                }
                cB()
            }
            function a9(cy, cx, cz) {
                if (!ca && cy) {
                    b0(function() {
                        if (cq === "POST") {
                            cl(cy, cz)
                        } else {
                            bR(cy, cz)
                        }
                        bI(cx)
                    })
                }
                if (!aq) {
                    cv()
                } else {
                    bP()
                }
            }
            function bL(cx) {
                if (ca) {
                    return false
                }
                return (cx && cx.length)
            }
            function cu(cz, cx) {
                if (!bL(cz)) {
                    return
                }
                var cy = '{"requests":["?' + cz.join('","?') + '"]}';
                b0(function() {
                    cl(cy, null, false);
                    bI(cx)
                })
            }
            function aw(cx) {
                return aS + cx + "." + by + "." + aW
            }
            function bx() {
                if (aT) {
                    return "0"
                }
                if (!z(f.cookieEnabled)) {
                    var cx = aw("testcookie");
                    ct(cx, "1");
                    return ah(cx) === "1" ? "1": "0"
                }
                return f.cookieEnabled ? "1": "0"
            }
            function aQ() {
                aW = bz((cg || ce) + (aX || "/")).slice(0, 4)
            }
            function bp() {
                var cy = aw("cvar"),
                    cx = ah(cy);
                if (cx.length) {
                    cx = JSON2.parse(cx);
                    if (M(cx)) {
                        return cx
                    }
                }
                return {}
            }
            function b1() {
                if (at === false) {
                    at = bp()
                }
            }
            function cb() {
                return bz((f.userAgent || "") + (f.platform || "") + JSON2.stringify(co) + (new Date()).getTime() + Math.random()).slice(0, 16)
            }
            function b8() {
                var cz = new Date(),
                    cx = Math.round(cz.getTime() / 1000),
                    cy = aw("id"),
                    cC = ah(cy),
                    cB,
                    cA;
                if (cC) {
                    cB = cC.split(".");
                    cB.unshift("0");
                    if (bh.length) {
                        cB[1] = bh
                    }
                    return cB
                }
                if (bh.length) {
                    cA = bh
                } else {
                    if ("0" === bx()) {
                        cA = ""
                    } else {
                        cA = cb()
                    }
                }
                cB = ["1", cA, cx, 0, cx, "", ""];
                return cB
            }
            function aA() {
                var cE = b8(),
                    cA = cE[0],
                    cB = cE[1],
                    cy = cE[2],
                    cx = cE[3],
                    cC = cE[4],
                    cz = cE[5];
                if (!z(cE[6])) {
                    cE[6] = ""
                }
                var cD = cE[6];
                return {
                    newVisitor: cA,
                    uuid: cB,
                    createTs: cy,
                    visitCount: cx,
                    currentVisitTs: cC,
                    lastVisitTs: cz,
                    lastEcommerceOrderTs: cD
                }
            }
            function al() {
                var cA = new Date(),
                    cy = cA.getTime(),
                    cB = aA().createTs;
                var cx = parseInt(cB, 10);
                var cz = (cx * 1000) + b6 - cy;
                return cz
            }
            function ao(cx) {
                if (!by) {
                    return
                }
                var cz = new Date(),
                    cy = Math.round(cz.getTime() / 1000);
                if (!z(cx)) {
                    cx = aA()
                }
                var cA = cx.uuid + "." + cx.createTs + "." + cx.visitCount + "." + cy + "." + cx.lastVisitTs + "." + cx.lastEcommerceOrderTs;
                ct(aw("id"), cA, al(), aX, cg)
            }
            function bf() {
                var cx = ah(aw("ref"));
                if (cx.length) {
                    try {
                        cx = JSON2.parse(cx);
                        if (M(cx)) {
                            return cx
                        }
                    } catch(cy) {}
                }
                return ["", "", 0, ""]
            }
            function bq(cz, cy, cx) {
                ct(cz, "", -86400, cy, cx)
            }
            function a2(cy) {
                var cx = "testvalue";
                ct("test", cx, 10000, null, cy);
                if (ah("test") === cx) {
                    bq("test", null, cy);
                    return true
                }
                return false
            }
            function aj() {
                var cz = aT;
                aT = false;
                var cx = ["id", "ses", "cvar", "ref"];
                var cy, cA;
                for (cy = 0; cy < cx.length; cy++) {
                    cA = aw(cx[cy]);
                    if (0 !== ah(cA)) {
                        bq(cA, aX, cg)
                    }
                }
                aT = cz
            }
            function bv(cx) {
                by = cx;
                ao()
            }
            function cw(cB) {
                if (!cB || !M(cB)) {
                    return
                }
                var cA = [];
                var cz;
                for (cz in cB) {
                    if (Object.prototype.hasOwnProperty.call(cB, cz)) {
                        cA.push(cz)
                    }
                }
                var cC = {};
                cA.sort();
                var cx = cA.length;
                var cy;
                for (cy = 0; cy < cx; cy++) {
                    cC[cA[cy]] = cB[cA[cy]]
                }
                return cC
            }
            function bF() {
                ct(aw("ses"), "*", bQ, aX, cg)
            }
            function bT(cz, cU, cV, cA) {
                var cT, cy = new Date(),
                    cH = Math.round(cy.getTime() / 1000),
                    cE,
                    cS,
                    cB = 1024,
                    c0,
                    cI,
                    cQ = at,
                    cC = aw("ses"),
                    cO = aw("ref"),
                    cL = aw("cvar"),
                    cM = ah(cC),
                    cR = bf(),
                    cX = aG || bg,
                    cF,
                    cx;
                if (aT) {
                    aj()
                }
                if (ca) {
                    return ""
                }
                var cN = aA();
                if (!z(cA)) {
                    cA = ""
                }
                var cK = x.characterSet || x.charset;
                if (!cK || cK.toLowerCase() === "utf-8") {
                    cK = null
                }
                cF = cR[0];
                cx = cR[1];
                cE = cR[2];
                cS = cR[3];
                if (!cM) {
                    var cW = bQ / 1000;
                    if (!cN.lastVisitTs || (cH - cN.lastVisitTs) > cW) {
                        cN.visitCount++;
                        cN.lastVisitTs = cN.currentVisitTs
                    }
                    if (!a1 || !cF.length) {
                        for (cT in bS) {
                            if (Object.prototype.hasOwnProperty.call(bS, cT)) {
                                cF = L(cX, bS[cT]);
                                if (cF.length) {
                                    break
                                }
                            }
                        }
                        for (cT in ba) {
                            if (Object.prototype.hasOwnProperty.call(ba, cT)) {
                                cx = L(cX, ba[cT]);
                                if (cx.length) {
                                    break
                                }
                            }
                        }
                    }
                    c0 = c(aV);
                    cI = cS.length ? c(cS) : "";
                    if (c0.length && !ay(c0) && (!a1 || !cI.length || ay(cI))) {
                        cS = aV
                    }
                    if (cS.length || cF.length) {
                        cE = cH;
                        cR = [cF, cx, cE, bu(cS.slice(0, cB))];
                        ct(cO, JSON2.stringify(cR), cm, aX, cg)
                    }
                }
                cz += "&idsite=" + by + "&rec=1&r=" + String(Math.random()).slice(2, 8) + "&h=" + cy.getHours() + "&m=" + cy.getMinutes() + "&s=" + cy.getSeconds() + "&url=" + n(bu(cX)) + (aV.length ? "&urlref=" + n(bu(aV)) : "") + ((a4 && a4.length) ? "&uid=" + n(a4) : "") + "&_id=" + cN.uuid + "&_idts=" + cN.createTs + "&_idvc=" + cN.visitCount + "&_idn=" + cN.newVisitor + (cF.length ? "&_rcn=" + n(cF) : "") + (cx.length ? "&_rck=" + n(cx) : "") + "&_refts=" + cE + "&_viewts=" + cN.lastVisitTs + (String(cN.lastEcommerceOrderTs).length ? "&_ects=" + cN.lastEcommerceOrderTs: "") + (String(cS).length ? "&_ref=" + n(bu(cS.slice(0, cB))) : "") + (cK ? "&cs=" + n(cK) : "") + "&send_image=0";
                for (cT in co) {
                    if (Object.prototype.hasOwnProperty.call(co, cT)) {
                        cz += "&" + cT + "=" + co[cT]
                    }
                }
                var cZ = [];
                if (cU) {
                    for (cT in cU) {
                        if (Object.prototype.hasOwnProperty.call(cU, cT) && /^dimension\d+$/.test(cT)) {
                            var cD = cT.replace("dimension", "");
                            cZ.push(parseInt(cD, 10));
                            cZ.push(String(cD));
                            cz += "&" + cT + "=" + cU[cT];
                            delete cU[cT]
                        }
                    }
                }
                if (cU && t(cU)) {
                    cU = null
                }
                for (cT in aU) {
                    if (Object.prototype.hasOwnProperty.call(aU, cT)) {
                        var cJ = ( - 1 === cZ.indexOf(cT));
                        if (cJ) {
                            cz += "&dimension" + cT + "=" + aU[cT]
                        }
                    }
                }
                if (cU) {
                    cz += "&data=" + n(JSON2.stringify(cU))
                } else {
                    if (Y) {
                        cz += "&data=" + n(JSON2.stringify(Y))
                    }
                }
                function cG(c1, c2) {
                    var c3 = JSON2.stringify(c1);
                    if (c3.length > 2) {
                        return "&" + c2 + "=" + n(c3)
                    }
                    return ""
                }
                var cY = cw(bo);
                var cP = cw(bN);
                cz += cG(cY, "cvar");
                cz += cG(cP, "e_cvar");
                if (at) {
                    cz += cG(at, "_cvar");
                    for (cT in cQ) {
                        if (Object.prototype.hasOwnProperty.call(cQ, cT)) {
                            if (at[cT][0] === "" || at[cT][1] === "") {
                                delete at[cT]
                            }
                        }
                    }
                    if (bj) {
                        ct(cL, JSON2.stringify(at), bQ, aX, cg)
                    }
                }
                if (aE) {
                    if (bO) {
                        cz += "&gt_ms=" + bO
                    } else {
                        if (g && g.timing && g.timing.requestStart && g.timing.responseEnd) {
                            cz += "&gt_ms=" + (g.timing.responseEnd - g.timing.requestStart)
                        }
                    }
                }
                cN.lastEcommerceOrderTs = z(cA) && String(cA).length ? cA: cN.lastEcommerceOrderTs;
                ao(cN);
                bF();
                cz += R(cV);
                if (ci.length) {
                    cz += "&" + ci
                }
                if (s(bD)) {
                    cz = bD(cz)
                }
                return cz
            }
            bk = function aI() {
                var cx = new Date();
                if (ch + aH <= cx.getTime()) {
                    var cy = bT("ping=1", null, "ping");
                    a9(cy, bc);
                    return true
                }
                return false
            };
            function aY(cA, cz, cE, cB, cx, cH) {
                var cC = "idgoal=0",
                    cD, cy = new Date(),
                    cF = [],
                    cG;
                if (String(cA).length) {
                    cC += "&ec_id=" + n(cA);
                    cD = Math.round(cy.getTime() / 1000)
                }
                cC += "&revenue=" + cz;
                if (String(cE).length) {
                    cC += "&ec_st=" + cE
                }
                if (String(cB).length) {
                    cC += "&ec_tx=" + cB
                }
                if (String(cx).length) {
                    cC += "&ec_sh=" + cx
                }
                if (String(cH).length) {
                    cC += "&ec_dt=" + cH
                }
                if (cj) {
                    for (cG in cj) {
                        if (Object.prototype.hasOwnProperty.call(cj, cG)) {
                            if (!z(cj[cG][1])) {
                                cj[cG][1] = ""
                            }
                            if (!z(cj[cG][2])) {
                                cj[cG][2] = ""
                            }
                            if (!z(cj[cG][3]) || String(cj[cG][3]).length === 0) {
                                cj[cG][3] = 0
                            }
                            if (!z(cj[cG][4]) || String(cj[cG][4]).length === 0) {
                                cj[cG][4] = 1
                            }
                            cF.push(cj[cG])
                        }
                    }
                    cC += "&ec_items=" + n(JSON2.stringify(cF))
                }
                cC = bT(cC, Y, "ecommerce", cD);
                a9(cC, bc)
            }
            function br(cx, cB, cA, cz, cy, cC) {
                if (String(cx).length && z(cB)) {
                    aY(cx, cB, cA, cz, cy, cC)
                }
            }
            function aZ(cx) {
                if (z(cx)) {
                    aY("", cx, "", "", "", "")
                }
            }
            function bs(cy, cz) {
                var cx = bT("action_name=" + n(X(cy || aR)), cz, "log");
                a9(cx, bc)
            }
            function aC(cz, cy) {
                var cA, cx = "(^| )(piwik[_-]" + cy;
                if (cz) {
                    for (cA = 0; cA < cz.length; cA++) {
                        cx += "|" + cz[cA]
                    }
                }
                cx += ")( |$)";
                return new RegExp(cx)
            }
            function ax(cx) {
                return (ai && cx && 0 === String(cx).indexOf(ai))
            }
            function bU(cB, cx, cC, cy) {
                if (ax(cx)) {
                    return 0
                }
                var cA = aC(be, "download"),
                    cz = aC(aJ, "link"),
                    cD = new RegExp("\\.(" + cn.join("|") + ")([?&#]|$)", "i");
                if (cz.test(cB)) {
                    return "link"
                }
                if (cy || cA.test(cB) || cD.test(cx)) {
                    return "download"
                }
                if (cC) {
                    return 0
                }
                return "link"
            }
            function ac(cy) {
                var cx;
                cx = cy.parentNode;
                while (cx !== null && z(cx)) {
                    if (S.isLinkElement(cy)) {
                        break
                    }
                    cy = cx;
                    cx = cy.parentNode
                }
                return cy
            }
            function cr(cC) {
                cC = ac(cC);
                if (!S.hasNodeAttribute(cC, "href")) {
                    return
                }
                if (!z(cC.href)) {
                    return
                }
                var cB = S.getAttributeValueFromNode(cC, "href");
                if (ax(cB)) {
                    return
                }
                var cy = cC.pathname || bM(cC.href);
                var cD = cC.hostname || c(cC.href);
                var cE = cD.toLowerCase();
                var cz = cC.href.replace(cD, cE);
                var cA = new RegExp("^(javascript|vbscript|jscript|mocha|livescript|ecmascript|mailto|tel):", "i");
                if (!cA.test(cz)) {
                    var cx = bU(cC.className, cz, ab(cE, cy), S.hasNodeAttribute(cC, "download"));
                    if (cx) {
                        return {
                            type: cx,
                            href: cz
                        }
                    }
                }
            }
            function ar(cx, cy, cz, cA) {
                var cB = p.buildInteractionRequestParams(cx, cy, cz, cA);
                if (!cB) {
                    return
                }
                return bT(cB, null, "contentInteraction")
            }
            function b7(cz, cA, cE, cx, cy) {
                if (!z(cz)) {
                    return
                }
                if (ax(cz)) {
                    return cz
                }
                var cC = p.toAbsoluteUrl(cz);
                var cB = "redirecturl=" + n(cC) + "&";
                cB += ar(cA, cE, cx, (cy || cz));
                var cD = "&";
                if (ai.indexOf("?") < 0) {
                    cD = "?"
                }
                return ai + cD + cB
            }
            function aM(cx, cy) {
                if (!cx || !cy) {
                    return false
                }
                var cz = p.findTargetNode(cx);
                if (p.shouldIgnoreInteraction(cz)) {
                    return false
                }
                cz = p.findTargetNodeNoDefault(cx);
                if (cz && !K(cz, cy)) {
                    return false
                }
                return true
            }
            function bV(cz, cy, cB) {
                if (!cz) {
                    return
                }
                var cx = p.findParentContentNode(cz);
                if (!cx) {
                    return
                }
                if (!aM(cx, cz)) {
                    return
                }
                var cA = p.buildContentBlock(cx);
                if (!cA) {
                    return
                }
                if (!cA.target && cB) {
                    cA.target = cB
                }
                return p.buildInteractionRequestParams(cy, cA.name, cA.piece, cA.target)
            }
            function az(cy) {
                if (!bC || !bC.length) {
                    return false
                }
                var cx, cz;
                for (cx = 0; cx < bC.length; cx++) {
                    cz = bC[cx];
                    if (cz && cz.name === cy.name && cz.piece === cy.piece && cz.target === cy.target) {
                        return true
                    }
                }
                return false
            }
            function a8(cA) {
                if (!cA) {
                    return false
                }
                var cD = p.findTargetNode(cA);
                if (!cD || p.shouldIgnoreInteraction(cD)) {
                    return false
                }
                var cE = cr(cD);
                if (cp && cE && cE.type) {
                    return false
                }
                if (S.isLinkElement(cD) && S.hasNodeAttributeWithValue(cD, "href")) {
                    var cx = String(S.getAttributeValueFromNode(cD, "href"));
                    if (0 === cx.indexOf("#")) {
                        return false
                    }
                    if (ax(cx)) {
                        return true
                    }
                    if (!p.isUrlToCurrentDomain(cx)) {
                        return false
                    }
                    var cB = p.buildContentBlock(cA);
                    if (!cB) {
                        return
                    }
                    var cz = cB.name;
                    var cF = cB.piece;
                    var cC = cB.target;
                    if (!S.hasNodeAttributeWithValue(cD, p.CONTENT_TARGET_ATTR) || cD.wasContentTargetAttrReplaced) {
                        cD.wasContentTargetAttrReplaced = true;
                        cC = p.toAbsoluteUrl(cx);
                        S.setAnyAttribute(cD, p.CONTENT_TARGET_ATTR, cC)
                    }
                    var cy = b7(cx, "click", cz, cF, cC);
                    p.setHrefAttribute(cD, cy);
                    return true
                }
                return false
            }
            function ap(cy) {
                if (!cy || !cy.length) {
                    return
                }
                var cx;
                for (cx = 0; cx < cy.length; cx++) {
                    a8(cy[cx])
                }
            }
            function aB(cx) {
                return function(cy) {
                    if (!cx) {
                        return
                    }
                    var cB = p.findParentContentNode(cx);
                    var cC;
                    if (cy) {
                        cC = cy.target || cy.srcElement
                    }
                    if (!cC) {
                        cC = cx
                    }
                    if (!aM(cB, cC)) {
                        return
                    }
                    bI(bc);
                    if (S.isLinkElement(cx) && S.hasNodeAttributeWithValue(cx, "href") && S.hasNodeAttributeWithValue(cx, p.CONTENT_TARGET_ATTR)) {
                        var cz = S.getAttributeValueFromNode(cx, "href");
                        if (!ax(cz) && cx.wasContentTargetAttrReplaced) {
                            S.setAnyAttribute(cx, p.CONTENT_TARGET_ATTR, "")
                        }
                    }
                    var cG = cr(cx);
                    if (Z && cG && cG.type) {
                        return cG.type
                    }
                    if (a8(cB)) {
                        return "href"
                    }
                    var cD = p.buildContentBlock(cB);
                    if (!cD) {
                        return
                    }
                    var cA = cD.name;
                    var cH = cD.piece;
                    var cF = cD.target;
                    var cE = ar("click", cA, cH, cF);
                    a9(cE, bc);
                    return cE
                }
            }
            function bt(cz) {
                if (!cz || !cz.length) {
                    return
                }
                var cx, cy;
                for (cx = 0; cx < cz.length; cx++) {
                    cy = p.findTargetNode(cz[cx]);
                    if (cy && !cy.contentInteractionTrackingSetupDone) {
                        cy.contentInteractionTrackingSetupDone = true;
                        W(cy, "click", aB(cy))
                    }
                }
            }
            function a3(cz, cA) {
                if (!cz || !cz.length) {
                    return []
                }
                var cx, cy;
                for (cx = 0; cx < cz.length; cx++) {
                    if (az(cz[cx])) {
                        cz.splice(cx, 1);
                        cx--
                    } else {
                        bC.push(cz[cx])
                    }
                }
                if (!cz || !cz.length) {
                    return []
                }
                ap(cA);
                bt(cA);
                var cB = [];
                for (cx = 0; cx < cz.length; cx++) {
                    cy = bT(p.buildImpressionRequestParams(cz[cx].name, cz[cx].piece, cz[cx].target), undefined, "contentImpressions");
                    if (cy) {
                        cB.push(cy)
                    }
                }
                return cB
            }
            function bY(cy) {
                var cx = p.collectContent(cy);
                return a3(cx, cy)
            }
            function aK(cy) {
                if (!cy || !cy.length) {
                    return []
                }
                var cx;
                for (cx = 0; cx < cy.length; cx++) {
                    if (!p.isNodeVisible(cy[cx])) {
                        cy.splice(cx, 1);
                        cx--
                    }
                }
                if (!cy || !cy.length) {
                    return []
                }
                return bY(cy)
            }
            function ak(cz, cx, cy) {
                var cA = p.buildImpressionRequestParams(cz, cx, cy);
                return bT(cA, null, "contentImpression")
            }
            function cs(cA, cy) {
                if (!cA) {
                    return
                }
                var cx = p.findParentContentNode(cA);
                var cz = p.buildContentBlock(cx);
                if (!cz) {
                    return
                }
                if (!cy) {
                    cy = "Unknown"
                }
                return ar(cy, cz.name, cz.piece, cz.target)
            }
            function b9(cy, cA, cx, cz) {
                return "e_c=" + n(cy) + "&e_a=" + n(cA) + (z(cx) ? "&e_n=" + n(cx) : "") + (z(cz) ? "&e_v=" + n(cz) : "")
            }
            function ad(cz, cB, cx, cA, cC) {
                if (String(cz).length === 0 || String(cB).length === 0) {
                    return false
                }
                var cy = bT(b9(cz, cB, cx, cA), cC, "event");
                a9(cy, bc)
            }
            function bB(cx, cA, cy, cB) {
                var cz = bT("search=" + n(cx) + (cA ? "&search_cat=" + n(cA) : "") + (z(cy) ? "&search_count=" + cy: ""), cB, "sitesearch");
                a9(cz, bc)
            }
            function cd(cx, cA, cz) {
                var cy = bT("idgoal=" + cx + (cA ? "&revenue=" + cA: ""), cz, "goal");
                a9(cy, bc)
            }
            function ck(cA, cx, cE, cD, cz) {
                var cC = cx + "=" + n(bu(cA));
                var cy = bV(cz, "click", cA);
                if (cy) {
                    cC += "&" + cy
                }
                var cB = bT(cC, cE, "link");
                a9(cB, (cD ? 0 : bc), cD)
            }
            function bl(cy, cx) {
                if (cy !== "") {
                    return cy + cx.charAt(0).toUpperCase() + cx.slice(1)
                }
                return cx
            }
            function bJ(cC) {
                var cB, cx, cA = ["", "webkit", "ms", "moz"],
                    cz;
                if (!aP) {
                    for (cx = 0; cx < cA.length; cx++) {
                        cz = cA[cx];
                        if (Object.prototype.hasOwnProperty.call(x, bl(cz, "hidden"))) {
                            if (x[bl(cz, "visibilityState")] === "prerender") {
                                cB = true
                            }
                            break
                        }
                    }
                }
                if (cB) {
                    W(x, cz + "visibilitychange",
                        function cy() {
                            x.removeEventListener(cz + "visibilitychange", cy, false);
                            cC()
                        });
                    return
                }
                cC()
            }
            function an(cx) {
                if (x.readyState === "complete") {
                    cx()
                } else {
                    if (J.addEventListener) {
                        J.addEventListener("load", cx)
                    } else {
                        if (J.attachEvent) {
                            J.attachEvent("onload", cx)
                        }
                    }
                }
            }
            function aN(cA) {
                var cx = false;
                if (x.attachEvent) {
                    cx = x.readyState === "complete"
                } else {
                    cx = x.readyState !== "loading"
                }
                if (cx) {
                    cA();
                    return
                }
                var cz;
                if (x.addEventListener) {
                    W(x, "DOMContentLoaded",
                        function cy() {
                            x.removeEventListener("DOMContentLoaded", cy, false);
                            if (!cx) {
                                cx = true;
                                cA()
                            }
                        })
                } else {
                    if (x.attachEvent) {
                        x.attachEvent("onreadystatechange",
                            function cy() {
                                if (x.readyState === "complete") {
                                    x.detachEvent("onreadystatechange", cy);
                                    if (!cx) {
                                        cx = true;
                                        cA()
                                    }
                                }
                            });
                        if (x.documentElement.doScroll && J === J.top) { (function cy() {
                            if (!cx) {
                                try {
                                    x.documentElement.doScroll("left")
                                } catch(cB) {
                                    setTimeout(cy, 0);
                                    return
                                }
                                cx = true;
                                cA()
                            }
                        } ())
                        }
                    }
                }
                W(J, "load",
                    function() {
                        if (!cx) {
                            cx = true;
                            cA()
                        }
                    },
                    false)
            }
            function b4(cx) {
                var cy = cr(cx);
                if (cy && cy.type) {
                    cy.href = k(cy.href);
                    ck(cy.href, cy.type, undefined, null, cx)
                }
            }
            function bX() {
                return x.all && !x.addEventListener
            }
            function cf(cx) {
                var cz = cx.which;
                var cy = (typeof cx.button);
                if (!cz && cy !== "undefined") {
                    if (bX()) {
                        if (cx.button & 1) {
                            cz = 1
                        } else {
                            if (cx.button & 2) {
                                cz = 3
                            } else {
                                if (cx.button & 4) {
                                    cz = 2
                                }
                            }
                        }
                    } else {
                        if (cx.button === 0 || cx.button === "0") {
                            cz = 1
                        } else {
                            if (cx.button & 1) {
                                cz = 2
                            } else {
                                if (cx.button & 2) {
                                    cz = 3
                                }
                            }
                        }
                    }
                }
                return cz
            }
            function bm(cx) {
                switch (cf(cx)) {
                    case 1:
                        return "left";
                    case 2:
                        return "middle";
                    case 3:
                        return "right"
                }
            }
            function aD(cx) {
                return cx.target || cx.srcElement
            }
            function ag(cx) {
                return function(cA) {
                    cA = cA || J.event;
                    var cz = bm(cA);
                    var cB = aD(cA);
                    if (cA.type === "click") {
                        var cy = false;
                        if (cx && cz === "middle") {
                            cy = true
                        }
                        if (cB && !cy) {
                            b4(cB)
                        }
                    } else {
                        if (cA.type === "mousedown") {
                            if (cz === "middle" && cB) {
                                au = cz;
                                a6 = cB
                            } else {
                                au = a6 = null
                            }
                        } else {
                            if (cA.type === "mouseup") {
                                if (cz === au && cB === a6) {
                                    b4(cB)
                                }
                                au = a6 = null
                            } else {
                                if (cA.type === "contextmenu") {
                                    b4(cB)
                                }
                            }
                        }
                    }
                }
            }
            function aa(cy, cx) {
                W(cy, "click", ag(cx), false);
                if (cx) {
                    W(cy, "mouseup", ag(cx), false);
                    W(cy, "mousedown", ag(cx), false);
                    W(cy, "contextmenu", ag(cx), false)
                }
            }
            function a7(cy) {
                if (!Z) {
                    Z = true;
                    var cz, cx = aC(a5, "ignore"),
                        cA = x.links;
                    if (cA) {
                        for (cz = 0; cz < cA.length; cz++) {
                            if (!cx.test(cA[cz].className)) {
                                aa(cA[cz], cy)
                            }
                        }
                    }
                }
            }
            function av(cz, cB, cC) {
                if (bH) {
                    return true
                }
                bH = true;
                var cD = false;
                var cA, cy;
                function cx() {
                    cD = true
                }
                an(function() {
                    function cE(cG) {
                        setTimeout(function() {
                                if (!bH) {
                                    return
                                }
                                cD = false;
                                cC.trackVisibleContentImpressions();
                                cE(cG)
                            },
                            cG)
                    }
                    function cF(cG) {
                        setTimeout(function() {
                                if (!bH) {
                                    return
                                }
                                if (cD) {
                                    cD = false;
                                    cC.trackVisibleContentImpressions()
                                }
                                cF(cG)
                            },
                            cG)
                    }
                    if (cz) {
                        cA = ["scroll", "resize"];
                        for (cy = 0; cy < cA.length; cy++) {
                            if (x.addEventListener) {
                                x.addEventListener(cA[cy], cx)
                            } else {
                                J.attachEvent("on" + cA[cy], cx)
                            }
                        }
                        cF(100)
                    }
                    if (cB && cB > 0) {
                        cB = parseInt(cB, 10);
                        cE(cB)
                    }
                })
            }
            function b2() {
                var cz, cB, cC = {
                        pdf: "application/pdf",
                        qt: "video/quicktime",
                        realp: "audio/x-pn-realaudio-plugin",
                        wma: "application/x-mplayer2",
                        dir: "application/x-director",
                        fla: "application/x-shockwave-flash",
                        java: "application/x-java-vm",
                        gears: "application/x-googlegears",
                        ag: "application/x-silverlight"
                    },
                    cy = J.devicePixelRatio || 1;
                if (! ((new RegExp("MSIE")).test(f.userAgent))) {
                    if (f.mimeTypes && f.mimeTypes.length) {
                        for (cz in cC) {
                            if (Object.prototype.hasOwnProperty.call(cC, cz)) {
                                cB = f.mimeTypes[cC[cz]];
                                co[cz] = (cB && cB.enabledPlugin) ? "1": "0"
                            }
                        }
                    }
                    if (typeof navigator.javaEnabled !== "unknown" && z(f.javaEnabled) && f.javaEnabled()) {
                        co.java = "1"
                    }
                    if (s(J.GearsFactory)) {
                        co.gears = "1"
                    }
                    co.cookie = bx()
                }
                var cA = parseInt(N.width, 10) * cy;
                var cx = parseInt(N.height, 10) * cy;
                co.res = parseInt(cA, 10) + "x" + parseInt(cx, 10)
            }
            b2();
            aQ();
            ao();
            return {
                getVisitorId: function() {
                    return aA().uuid
                },
                getVisitorInfo: function() {
                    return b8()
                },
                getAttributionInfo: function() {
                    return bf()
                },
                getAttributionCampaignName: function() {
                    return bf()[0]
                },
                getAttributionCampaignKeyword: function() {
                    return bf()[1]
                },
                getAttributionReferrerTimestamp: function() {
                    return bf()[2]
                },
                getAttributionReferrerUrl: function() {
                    return bf()[3]
                },
                setTrackerUrl: function(cx) {
                    ai = cx
                },
                getTrackerUrl: function() {
                    return ai
                },
                getSiteId: function() {
                    return by
                },
                setSiteId: function(cx) {
                    bv(cx)
                },
                setUserId: function(cx) {
                    if (!z(cx) || !cx.length) {
                        return
                    }
                    a4 = cx;
                    bh = bz(a4).substr(0, 16)
                },
                getUserId: function() {
                    return a4
                },
                setCustomData: function(cx, cy) {
                    if (M(cx)) {
                        Y = cx
                    } else {
                        if (!Y) {
                            Y = {}
                        }
                        Y[cx] = cy
                    }
                },
                getCustomData: function() {
                    return Y
                },
                setCustomRequestProcessing: function(cx) {
                    bD = cx
                },
                appendToTrackingUrl: function(cx) {
                    ci = cx
                },
                getRequest: function(cx) {
                    return bT(cx)
                },
                addPlugin: function(cx, cy) {
                    a[cx] = cy
                },
                setCustomDimension: function(cx, cy) {
                    cx = parseInt(cx, 10);
                    if (cx > 0) {
                        if (!z(cy)) {
                            cy = ""
                        }
                        if (!q(cy)) {
                            cy = String(cy)
                        }
                        aU[cx] = cy
                    }
                },
                getCustomDimension: function(cx) {
                    cx = parseInt(cx, 10);
                    if (cx > 0 && Object.prototype.hasOwnProperty.call(aU, cx)) {
                        return aU[cx]
                    }
                },
                deleteCustomDimension: function(cx) {
                    cx = parseInt(cx, 10);
                    if (cx > 0) {
                        delete aU[cx]
                    }
                },
                setCustomVariable: function(cy, cx, cB, cz) {
                    var cA;
                    if (!z(cz)) {
                        cz = "visit"
                    }
                    if (!z(cx)) {
                        return
                    }
                    if (!z(cB)) {
                        cB = ""
                    }
                    if (cy > 0) {
                        cx = !q(cx) ? String(cx) : cx;
                        cB = !q(cB) ? String(cB) : cB;
                        cA = [cx.slice(0, a0), cB.slice(0, a0)];
                        if (cz === "visit" || cz === 2) {
                            b1();
                            at[cy] = cA
                        } else {
                            if (cz === "page" || cz === 3) {
                                bo[cy] = cA
                            } else {
                                if (cz === "event") {
                                    bN[cy] = cA
                                }
                            }
                        }
                    }
                },
                getCustomVariable: function(cy, cz) {
                    var cx;
                    if (!z(cz)) {
                        cz = "visit"
                    }
                    if (cz === "page" || cz === 3) {
                        cx = bo[cy]
                    } else {
                        if (cz === "event") {
                            cx = bN[cy]
                        } else {
                            if (cz === "visit" || cz === 2) {
                                b1();
                                cx = at[cy]
                            }
                        }
                    }
                    if (!z(cx) || (cx && cx[0] === "")) {
                        return false
                    }
                    return cx
                },
                deleteCustomVariable: function(cx, cy) {
                    if (this.getCustomVariable(cx, cy)) {
                        this.setCustomVariable(cx, "", "", cy)
                    }
                },
                storeCustomVariablesInCookie: function() {
                    bj = true
                },
                setLinkTrackingTimer: function(cx) {
                    bc = cx
                },
                setDownloadExtensions: function(cx) {
                    if (q(cx)) {
                        cx = cx.split("|")
                    }
                    cn = cx
                },
                addDownloadExtensions: function(cy) {
                    var cx;
                    if (q(cy)) {
                        cy = cy.split("|")
                    }
                    for (cx = 0; cx < cy.length; cx++) {
                        cn.push(cy[cx])
                    }
                },
                removeDownloadExtensions: function(cz) {
                    var cy, cx = [];
                    if (q(cz)) {
                        cz = cz.split("|")
                    }
                    for (cy = 0; cy < cn.length; cy++) {
                        if (C(cz, cn[cy]) === -1) {
                            cx.push(cn[cy])
                        }
                    }
                    cn = cx
                },
                setDomains: function(cx) {
                    ae = q(cx) ? [cx] : cx;
                    var cz = false,
                        cy;
                    for (cy in ae) {
                        if (Object.prototype.hasOwnProperty.call(ae, cy) && b3(ce, B(String(ae[cy])))) {
                            cz = true
                        }
                    }
                    if (!cz) {
                        ae.push(ce)
                    }
                },
                setIgnoreClasses: function(cx) {
                    a5 = q(cx) ? [cx] : cx
                },
                setRequestMethod: function(cx) {
                    cq = cx || bK
                },
                setRequestContentType: function(cx) {
                    bW = cx || am
                },
                setReferrerUrl: function(cx) {
                    aV = cx
                },
                setCustomUrl: function(cx) {
                    aG = bn(bg, cx)
                },
                setDocumentTitle: function(cx) {
                    aR = cx
                },
                setAPIUrl: function(cx) {
                    bb = cx
                },
                setDownloadClasses: function(cx) {
                    be = q(cx) ? [cx] : cx
                },
                setLinkClasses: function(cx) {
                    aJ = q(cx) ? [cx] : cx
                },
                setCampaignNameKey: function(cx) {
                    bS = q(cx) ? [cx] : cx
                },
                setCampaignKeywordKey: function(cx) {
                    ba = q(cx) ? [cx] : cx
                },
                discardHashTag: function(cx) {
                    bi = cx
                },
                setCookieNamePrefix: function(cx) {
                    aS = cx;
                    at = bp()
                },
                setCookieDomain: function(cx) {
                    var cy = B(cx);
                    if (a2(cy)) {
                        cg = cy;
                        aQ()
                    }
                },
                setCookiePath: function(cx) {
                    aX = cx;
                    aQ()
                },
                setVisitorCookieTimeout: function(cx) {
                    b6 = cx * 1000
                },
                setSessionCookieTimeout: function(cx) {
                    bQ = cx * 1000
                },
                setReferralCookieTimeout: function(cx) {
                    cm = cx * 1000
                },
                setConversionAttributionFirstReferrer: function(cx) {
                    a1 = cx
                },
                disableCookies: function() {
                    aT = true;
                    co.cookie = "0";
                    if (by) {
                        aj()
                    }
                },
                deleteCookies: function() {
                    aj()
                },
                setDoNotTrack: function(cy) {
                    var cx = f.doNotTrack || f.msDoNotTrack;
                    ca = cy && (cx === "yes" || cx === "1");
                    if (ca) {
                        this.disableCookies()
                    }
                },
                addListener: function(cy, cx) {
                    aa(cy, cx)
                },
                enableLinkTracking: function(cx) {
                    cp = true;
                    bJ(function() {
                        aN(function() {
                            a7(cx)
                        })
                    })
                },
                enableJSErrorTracking: function() {
                    if (cc) {
                        return
                    }
                    cc = true;
                    var cx = J.onerror;
                    J.onerror = function(cC, cA, cz, cB, cy) {
                        bJ(function() {
                            var cD = "JavaScript Errors";
                            var cE = cA + ":" + cz;
                            if (cB) {
                                cE += ":" + cB
                            }
                            ad(cD, cE, cC)
                        });
                        if (cx) {
                            return cx(cC, cA, cz, cB, cy)
                        }
                        return false
                    }
                },
                disablePerformanceTracking: function() {
                    aE = false
                },
                setGenerationTimeMs: function(cx) {
                    bO = parseInt(cx, 10)
                },
                enableHeartBeatTimer: function(cx) {
                    cx = Math.max(cx, 1);
                    aH = (cx || 15) * 1000;
                    if (ch !== null) {
                        cv()
                    }
                },
                killFrame: function() {
                    if (J.location !== J.top.location) {
                        J.top.location = J.location
                    }
                },
                redirectFile: function(cx) {
                    if (J.location.protocol === "file:") {
                        J.location = cx
                    }
                },
                setCountPreRendered: function(cx) {
                    aP = cx
                },
                trackGoal: function(cx, cz, cy) {
                    bJ(function() {
                        cd(cx, cz, cy)
                    })
                },
                trackLink: function(cy, cx, cA, cz) {
                    bJ(function() {
                        ck(cy, cx, cA, cz)
                    })
                },
                trackPageView: function(cx, cy) {
                    bC = [];
                    if (D(by)) {
                        bJ(function() {
                            P(ai, bb, by)
                        })
                    } else {
                        bJ(function() {
                            bs(cx, cy)
                        })
                    }
                },
                trackAllContentImpressions: function() {
                    if (D(by)) {
                        return
                    }
                    bJ(function() {
                        aN(function() {
                            var cx = p.findContentNodes();
                            var cy = bY(cx);
                            cu(cy, bc)
                        })
                    })
                },
                trackVisibleContentImpressions: function(cx, cy) {
                    if (D(by)) {
                        return
                    }
                    if (!z(cx)) {
                        cx = true
                    }
                    if (!z(cy)) {
                        cy = 750
                    }
                    av(cx, cy, this);
                    bJ(function() {
                        an(function() {
                            var cz = p.findContentNodes();
                            var cA = aK(cz);
                            cu(cA, bc)
                        })
                    })
                },
                trackContentImpression: function(cz, cx, cy) {
                    if (D(by)) {
                        return
                    }
                    if (!cz) {
                        return
                    }
                    cx = cx || "Unknown";
                    bJ(function() {
                        var cA = ak(cz, cx, cy);
                        a9(cA, bc)
                    })
                },
                trackContentImpressionsWithinNode: function(cx) {
                    if (D(by) || !cx) {
                        return
                    }
                    bJ(function() {
                        if (bH) {
                            an(function() {
                                var cy = p.findContentNodesWithinNode(cx);
                                var cz = aK(cy);
                                cu(cz, bc)
                            })
                        } else {
                            aN(function() {
                                var cy = p.findContentNodesWithinNode(cx);
                                var cz = bY(cy);
                                cu(cz, bc)
                            })
                        }
                    })
                },
                trackContentInteraction: function(cz, cA, cx, cy) {
                    if (D(by)) {
                        return
                    }
                    if (!cz || !cA) {
                        return
                    }
                    cx = cx || "Unknown";
                    bJ(function() {
                        var cB = ar(cz, cA, cx, cy);
                        a9(cB, bc)
                    })
                },
                trackContentInteractionNode: function(cy, cx) {
                    if (D(by) || !cy) {
                        return
                    }
                    bJ(function() {
                        var cz = cs(cy, cx);
                        a9(cz, bc)
                    })
                },
                logAllContentBlocksOnPage: function() {
                    var cy = p.findContentNodes();
                    var cx = p.collectContent(cy);
                    if (console !== undefined && console && console.log) {
                        console.log(cx)
                    }
                },
                trackEvent: function(cy, cA, cx, cz, cB) {
                    bJ(function() {
                        ad(cy, cA, cx, cz, cB)
                    })
                },
                trackSiteSearch: function(cx, cz, cy, cA) {
                    bJ(function() {
                        bB(cx, cz, cy, cA)
                    })
                },
                setEcommerceView: function(cA, cx, cz, cy) {
                    if (!z(cz) || !cz.length) {
                        cz = ""
                    } else {
                        if (cz instanceof Array) {
                            cz = JSON2.stringify(cz)
                        }
                    }
                    bo[5] = ["_pkc", cz];
                    if (z(cy) && String(cy).length) {
                        bo[2] = ["_pkp", cy]
                    }
                    if ((!z(cA) || !cA.length) && (!z(cx) || !cx.length)) {
                        return
                    }
                    if (z(cA) && cA.length) {
                        bo[3] = ["_pks", cA]
                    }
                    if (!z(cx) || !cx.length) {
                        cx = ""
                    }
                    bo[4] = ["_pkn", cx]
                },
                addEcommerceItem: function(cB, cx, cz, cy, cA) {
                    if (cB.length) {
                        cj[cB] = [cB, cx, cz, cy, cA]
                    }
                },
                trackEcommerceOrder: function(cx, cB, cA, cz, cy, cC) {
                    br(cx, cB, cA, cz, cy, cC)
                },
                trackEcommerceCartUpdate: function(cx) {
                    aZ(cx)
                }
            }
        }
        function y() {
            return {
                push: T
            }
        }
        function b(ad, ac) {
            var ae = {};
            var aa, ab;
            for (aa = 0; aa < ac.length; aa++) {
                var Y = ac[aa];
                ae[Y] = 1;
                for (ab = 0; ab < ad.length; ab++) {
                    if (ad[ab] && ad[ab][0]) {
                        var Z = ad[ab][0];
                        if (Y === Z) {
                            T(ad[ab]);
                            delete ad[ab];
                            if (ae[Z] > 1) {
                                if (console !== undefined && console && console.error) {
                                    console.error("The method " + Z + ' is registered more than once in "paq" variable. Only the last call has an effect. Please have a look at the multiple Piwik trackers documentation: http://developer.piwik.org/guides/tracking-javascript-guide#multiple-piwik-trackers')
                                }
                            }
                            ae[Z]++
                        }
                    }
                }
            }
            return ad
        }
        W(J, "beforeunload", U, false);
        Date.prototype.getTimeAlias = Date.prototype.getTime;
        O = new G();
        var u = ["disableCookies", "setTrackerUrl", "setAPIUrl", "setCookiePath", "setCookieDomain", "setDomains", "setUserId", "setSiteId", "enableLinkTracking"];
        _paq = b(_paq, u);
        for (w = 0; w < _paq.length; w++) {
            if (_paq[w]) {
                T(_paq[w])
            }
        }
        _paq = new y();
        d = {
            addPlugin: function(Y, Z) {
                a[Y] = Z
            },
            getTracker: function(Y, Z) {
                if (!z(Z)) {
                    Z = this.getAsyncTracker().getSiteId()
                }
                if (!z(Y)) {
                    Y = this.getAsyncTracker().getTrackerUrl()
                }
                return new G(Y, Z)
            },
            getAsyncTracker: function() {
                return O
            }
        };
        if (typeof define === "function" && define.amd) {
            define("piwik", [],
                function() {
                    return d
                })
        }
        return d
    } ())
}
if (window && window.piwikAsyncInit) {
    window.piwikAsyncInit()
} (function() {
    var a = (typeof AnalyticsTracker);
    if (a === "undefined") {
        AnalyticsTracker = window.Piwik
    }
} ());
if (typeof piwik_log !== "function") {
    piwik_log = function(b, f, d, g) {
        function a(h) {
            try {
                if (window["piwik_" + h]) {
                    return window["piwik_" + h]
                }
            } catch(i) {}
            return
        }
        var c, e = window.Piwik.getTracker(d, f);
        e.setDocumentTitle(b);
        e.setCustomData(g);
        c = a("tracker_pause");
        if (c) {
            e.setLinkTrackingTimer(c)
        }
        c = a("download_extensions");
        if (c) {
            e.setDownloadExtensions(c)
        }
        c = a("hosts_alias");
        if (c) {
            e.setDomains(c)
        }
        c = a("ignore_classes");
        if (c) {
            e.setIgnoreClasses(c)
        }
        e.trackPageView();
        if (a("install_tracker")) {
            piwik_track = function(i, k, j, h) {
                e.setSiteId(k);
                e.setTrackerUrl(j);
                e.trackLink(i, h)
            };
            e.enableLinkTracking()
        }
    };
    /*!! @license-end */
};
