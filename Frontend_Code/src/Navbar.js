import React, { useState, useEffect } from "react";
import swenfly from "./swenfly.jpg";
import "./style.css";
import { FiUser } from "react-icons/fi";
import Contact from "./Contact";
import LandingPage from "./LandingPage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

export default function Navbar() {
  const [activeButton, setActiveButton] = useState("");

  useEffect(() => {
    const navButton = window.navButton;
    setActiveButton(navButton);
  }, []);

  return (
    <Router>
      <div id="customNav" className="customNav">
        <nav className="navbar navbar-expand-lg bg-body-tertiary">
          <div className="container-fluid">
            <img
              src={swenfly}
              className="img me-3  swenFlyLogo"
              width="40px"
              height="40px"
              alt={swenfly}
            />
            <Link className="navbar-brand" to="/">
              SWENFLY
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
            <div
              className="collapse navbar-collapse"
              id="navbarSupportedContent"
            >
              <ul className="navbar-nav ms-auto">
                <li className="nav-item">
                  <Link
                    className={`nav-link ${
                      activeButton === "/LandingPage" ? "active" : ""
                    }`}
                    aria-current="page"
                    as={Link}
                    to="/LandingPage"
                    onClick={() => setActiveButton("/LandingPage")}
                  >
                    Home
                  </Link>
                </li>

                <li className="nav-item">
                  <Link
                    className={`nav-link ${
                      activeButton === "/Contact" ? "active" : ""
                    }`}
                    as={Link}
                    to="/Contact"
                    onClick={() => setActiveButton("/Contact")}
                  >
                    Contact
                  </Link>
                </li>
                <li className="nav-item">
                  <a className="nav-link me-3" href="/">
                    <FiUser />
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </nav>
      </div>
      <div>
        <Routes>
          <Route path="/Contact" element={<Contact />} />
          <Route path="/LandingPage" element={<LandingPage />} />
          <Route path="/" element={<LandingPage />} />
        </Routes>
      </div>
    </Router>
  );
}
