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
                    <OauthCallback/>
                </Route>
                <Route path="/logout">
                    <Logout/>
                </Route>
                <Route path="/">
                    <Home/>
                </Route>
            </Switch>
        </Router>
    );
}

function Home() {
    if (window.location.search.includes('logout')) {
        localStorage.removeItem('Token');
    }
    if (null != localStorage.getItem('Token')) {
        return index();
    }
    const getOauth2CodeUrl = 'http://localhost:10080/auth/oauth/authorize?client_id=sso-b&redirect_uri=http://localhost:3001/oauth_callback&response_type=code';
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>System B,Please Login</p>
                <a className="App-link" href={getOauth2CodeUrl}>
                    Oauth2 Login
                </a>
            </header>
        </div>
    );
}

function OauthCallback() {
    let data = new URLSearchParams();
    data.append('code', window.location.search.substring(6));
    data.append('grant_type', 'authorization_code');
    data.append('redirect_uri', 'http://localhost:3001/oauth_callback');

    fetch('http://localhost:10080/auth/oauth/token', {
        method: "POST",
        body: data,
        headers: {
            'Authorization': 'Basic c3NvLWI6MTIzNDU2',
            'content-type': 'application/x-www-form-urlencoded'
        }
    })
        .then((res) => {
            return res.json();
        })
        .then((result) => {
            localStorage.setItem('Token', JSON.stringify(result));
        })
        .catch((err) => {
            console.log(err);
        });

    return index();
}

function index() {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>Welcome to System B!</p>
                <a className="App-link" href="http://localhost:3000">
                    Visit System A
                </a>
                <a className="App-link" href="/logout">
                    Oauth2 Logout
                </a>
            </header>
        </div>
    );
}

function Logout() {
    const access_token = JSON.parse(localStorage.getItem('Token'))['access_token'];
    window.location.href =
        'http://localhost:10080/auth/token/exit?' +
        "callbackUrl=http://localhost:3001/" +
        "&token=" + access_token;
}