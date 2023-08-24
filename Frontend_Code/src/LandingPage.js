import "./style.css";
import "./App.css";
import Box from "./Box";
import swecom from "./swecom.jpg";
import React, { useRef } from "react";
//import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
// import  Footer from "./Footer"
const LandingPage = () => {
  const inputRef = useRef(null);

  const handleStartClick = () => {
    inputRef.current.focus();
  };
  return (
    <div className="Landing">
      <div class="row">
        <div class="col-sm-6">
          <div className="LandingText">
            <h1 className="heading">Your Lightning Fast</h1>
            <h1 className="heading1">URL Shortener</h1><br/>
            <h6>
              Convert your long URL to convert into Shortener <br />
              for faster website access <br />
              <button
                type="button"
                className="btn btn-primary startNow"
                onClick={handleStartClick}
              >
                Start Now
              </button>
            </h6>
          </div>
        </div>
        <div class="col-sm-6 laptopImage">
          <img src={swecom} className="laptop" alt={swecom} />
        </div>
        <Box inputRef={inputRef} />
      </div>
    </div>
  );
};

export default LandingPage;
