import React from 'react';
import logo from './logo.svg';
import './App.css';
import {
    BrowserRouter as Router,
    Switch,
    Route
} from "react-router-dom";

export default function App() {
    return (
        <Router>
            <Switch>
                <Route path="/oauth_callback">
                    <Oauth2CodeCallback/>
                </Route>
                <Route path="/">
                    <Home/>
                </Route>
            </Switch>
        </Router>
    );
}

function Home() {
    if (null != localStorage.getItem('Token')) {
        return index();
    }

    const state = randomString(8);
    localStorage.setItem('state', state);
    const getOauth2CodeUrl = AUTH_CENTER_URL + '/oauth/authorize' +
        '?client_id=sso-a' +
        '&state=' + state +
        '&redirect_uri=http://localhost:3000/oauth_callback' +
        '&response_type=code';

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>System A,Please Login</p>
                <a className="App-link" href={getOauth2CodeUrl}>
                    Oauth2 Login
                </a>
            </header>
        </div>
    );
}

function Oauth2CodeCallback() {
    getToken();
    return index();
}

function index() {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>Welcome to System A!</p>
                <button onClick={getResource}>Get Info From Resource</button>
                <button onClick={resourceServerLogout}>Logout</button>
                <a className="App-link" href="http://localhost:3001">
                    Visit System B
                </a>
            </header>
        </div>
    );
}

function authServerLogout() {
    if (!!localStorage.getItem('Token')) {
        let Token = JSON.parse(localStorage.getItem('Token'));
        if (!!Token && !!Token['access_token']) {
            const logoutUrl = AUTH_CENTER_URL + '/token/exit?token=' + Token['access_token'];

            fetch(logoutUrl, {
                method: "GET",
                credentials: 'include',
                headers: {
                    'content-type': 'application/json'
                }
            })
                .then((res) => {
                        return res.json();
                    }
                )
                .then((result) => {
                    console.log('sso-server logout result:', result);
                    if (result['data']) {
                        localStorage.removeItem('Token');
                        window.location.href = "/";
                    } else {
                        alert("登出错误：" + result['msg']);
                        window.location.href = "/";
                    }
                })
                .catch((err) => {
                    alert("登出错误：" + err);
                    window.location.href = "/";
                });
        }
    } else {
        window.location.href = "/";
    }
}

function resourceServerLogout() {
    const CRSFToken = localStorage.getItem('CRSFToken');
    const access_token = JSON.parse(localStorage.getItem('Token'))['access_token'];
    if (!!CRSFToken) {
        const logoutUrl = RESOURCE_URL + '/exit';
        fetch(logoutUrl, {
            method: "GET",
            credentials: 'include',
            headers: {
                'content-type': 'application/json',
                'Authorization': 'Bearer ' + access_token,
                'CRSFToken': CRSFToken
            }
        })
            .then((res) => {
                    return res.json();
                }
            )
            .then((result) => {
                console.log('resource-server logout result:', result);
                if (result['data']) {
                    localStorage.removeItem('CRSFToken');
                    authServerLogout();
                } else {
                    alert("登出业务服务器错误：" + result['msg']);
                }
            })
            .catch((err) => {
                alert("登出业务服务器错误：" + err);
            });
    }else{
        localStorage.removeItem('CRSFToken');
        authServerLogout();
    }
}

// ====================================================token相关====================================================

const AUTH_CENTER_URL = 'http://localhost:10080/auth';
const RESOURCE_URL = 'http://localhost:10081/resource';

function getResource() {
    // todo 等待返回后再处理
    new Promise((fulfill) => {
            fulfill(getCRSFToken());
        }
    )
        .then((value) => {
                if (!!!value) {
                    alert('无法登入业务服务器');
                    return Home();
                }
                const access_token = JSON.parse(localStorage.getItem('Token'))['access_token'];
                fetch('http://localhost:10081/resource/sso/info', {
                    method: "GET",
                    credentials: 'include',
                    headers: {
                        'Authorization': 'Bearer ' + access_token,
                        'content-type': 'application/json',
                        'CRSFToken': value
                    }
                })
                    .then((res) => {
                        return res.json();
                    })
                    .then((result) => {
                        alert(JSON.stringify(result));
                    })
                    .catch((err) => {
                        alert(err);
                    });
            }
        );
}

function getCRSFToken() {
    const CRSFToken = localStorage.getItem('CRSFToken');
    if (!!CRSFToken) {
        return CRSFToken;
    }
    const tokenStr = localStorage.getItem('Token');
    if (!tokenStr) {
        return null;
    }
    const token = JSON.parse(tokenStr);
    fetch(RESOURCE_URL + '/getCRSFToken', {
        method: "POST",
        credentials: 'include',
        headers: {
            'SaasDynamicSecret': getDynamicSecret(),
            'Authorization': 'Bearer ' + token['access_token'],
            'content-type': 'application/json'
        }
    })
        .then((res) => {
                return res.json();
            }
        )
        .then((result) => {
            if (result['success']) {
                localStorage.setItem('CRSFToken', result['CRSFToken']);
                return result['CRSFToken'];
            } else {
                alert('ssoCodeCheck:' + result);
                return null;
            }
        })
        .catch((err) => {
            alert(err);
            return null;
        });
}

function getToken() {
    let data = new URLSearchParams();
    let strings = window.location.search.split('&');
    const code = strings[0].substring('?code='.length);
    const state = strings[1].substring('state='.length);
    if (!state || !localStorage.getItem('state') || state !== localStorage.getItem('state')) {
        alert('Wrong state,invalid get token request');
        window.location.href = '/';
    }
    // 认证中心返回的验证码
    data.append('code', code);
    data.append('grant_type', 'authorization_code');
    data.append('redirect_uri', 'http://localhost:3000/oauth_callback');

    fetch(AUTH_CENTER_URL + '/oauth/token', {
        // 此处如果带上session(credentials: 'include')，会引发错误
        method: "POST",
        body: data,
        headers: {
            // c3NvLWE6MTIzNDU2是client_id:client_secret的base64编码
            'Authorization': 'Basic c3NvLWE6MTIzNDU2',
            'content-type': 'application/x-www-form-urlencoded',
        }
    })
        .then((res) => {
            return res.json();
        })
        .then((result) => {
            let resultString = JSON.stringify(result);
            if (resultString.includes('access_token')) {
                localStorage.setItem('Token', resultString);
                window.location.href = '/';
            } else {
                alert("登录失败：" + resultString);
                window.location.href = '/';
            }
        })
        .catch((err) => {
            alert(err);
            window.location.href = '/';
        });
    localStorage.removeItem('state');
}

function getDynamicSecret() {
    // TODO 动态库获取
    return 'abc123';
}

function randomString(length) {
    const chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let result = '';
    for (let i = length; i > 0; --i) {
        result += chars[Math.floor(Math.random() * chars.length)];
    }
    return result;
}