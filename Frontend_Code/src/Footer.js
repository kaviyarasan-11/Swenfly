import React from "react";
import swenfly from "./swenfly.jpg";
import "./footer.css";
export default function Footer() {
  return (
    <div>
      <footer class="footer">
        <div class="container">
          <div class="row">
            <p className="copy">
              Copyright <span>&copy;</span> 2023 All rights reserved.
            </p>
            <div class="footer-col">
              {/* <img src="./swenfly.jpg" alt="" style={{"display":"inline", "height":"25px", borderRadius:"10px"}}/> */}
              {/* <img className="footerLogo"
                src={swenfly}
                alt=""
                style={{ height: "30px", width: "30px", "margin-right": "5px",borderRadius:"10px" }}
              /><span>swenfly</span> */}

              {/* <p>
                Lorem ipsum dolor sit amet consectetur adipisicing elit.
                Expedita, magnam maxime tempora reprehenderit corporis
                exercitationem error sint soluta sed dicta neque at vel modi
                eius iure cum consequuntur, esse porro?
              </p> */}
            </div>
            <div class="footer-col">
              {/* <h4>Quick Links</h4> */}
              {/* <ul>
                <li>
                  <a href="/">Home</a>
                </li>
                <li>
                  <a href="/">About Us</a>
                </li>
                <li>
                  <a href="/">Resources</a>
                </li>
                <li>
                  <a href="/">Contact Us</a>
                </li>
                <li>
                  <a href="/">Mobile App</a>
                </li>
                <li>
                  <a href="/">Blogs page</a>
                </li>
              </ul> */}
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
