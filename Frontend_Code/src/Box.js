import { useState } from "react";
import axios from "axios";
import QRCode from "qrcode.react";
import CopyToClipboard from "react-copy-to-clipboard";
import { FiCopy, FiMail } from "react-icons/fi";
import { Link } from "react-router-dom";
// import { FiShare2 } from "react-icons/fi";
import { RiShareForwardLine } from "react-icons/ri";
import "./style.css";
export const Box = ({inputRef}) => {
  const [longUrl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [qrCodeValue, setQRCodeValue] = useState("https://play.google.com/store/apps/details?id=com.shorturl.app");
  const [toAddress, settoAddress] = useState("");
  const [copied, setCopied] = useState(false);

  // Long to short url on submit
  const handleSubmit = async (event) => {
    event.preventDefault();
    //returns nothing if no input is passed to long url
    if (!longUrl) {
      return;
    }
    //generates short url
    try {
      const response = await axios.post("https://sw8.in/generate-short-url", {
        longUrl,
      });

      setShortUrl(response.data.shortUrl);
      setQRCodeValue(response.data.shortUrl); //creates qr for shorturl
    } catch (error) {
      console.error(error);
    }
  };
  //email toggle
  const [showInput, setShowInput] = useState(false);

  // clear both input field and short url
  const handleReset = () => {
    setLongUrl("");
    setShortUrl("");
    setQRCodeValue("");
    settoAddress("");
  };

  const handleCopy = () => {
    setCopied(true);
    setTimeout(() => {
      setCopied(false);
    }, 1500);
  };
  // send toAddress handler

  // const handleSendtoAddress = async (event) => {
  //   event.preventDefault();

  //   const response = await fetch("https://sw8.in/share-via-email", {
  //     method: "POST",
  //     headers: { "Content-Type": "application/json" },
  //     body: JSON.stringify({ longUrl, shortUrl, toAddress }),
  //   });
  //   const data = await response.json();
  //   console.log(data.message);
  // };

  //subbu akka modified code
  const handleSendtoAddress = async (event) => {
    event.preventDefault();

    try {
      const response = await fetch("https://sw8.in/share-via-email", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ longUrl, shortUrl, toAddress }),
      });

      if (response.ok) {
        console.log("Email sent successfully.");
        // Reset form fields
        settoAddress("");
        setLongUrl("");
        setShortUrl("");
      } else {
        console.error("Failed to send email.");
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="BoxHead">
      <div className="Box">
        <div className="generatorbox d-flex  justify-content-center  mt-5">
          <div className="postcardbox border border-1 shadow-lg p-3 mb-5 bg-body-tertiary rounded  d-flex flex-column text-center">
            <div className="postcaption ">
              <h3>
                <b>Paste Your Long URL </b>
              </h3>
            </div>
            <div className="smallcaption">
              <p>
                Our URL shortener allows to generate a short URL making it
                easily shareable
              </p>
            </div>
            <div className="generatinginput">
              <form onSubmit={handleSubmit}>
                <input
                ref = {inputRef}
                  value={longUrl}
                  onChange={(event) => setLongUrl(event.target.value)}
                  type="text"
                  className="inputgen rounded-start-2"
                  style={{ width: "80%", borderColor: "#0275d8" }}
                  placeholder=""
                />

                <button
                  type="submit"
                  className="generatebtn bg-primary border-0 rounded-end  text-light"
                  style={{ height: "1.77em" }}
                >
                  Generate
                </button>
                <br />
                <br />

                <input type="text" value={shortUrl} readOnly />
                <button
                  type="button" className="generatebtn bg-primary border-0 rounded-end  text-light"
                  style={{ height: "1.77em" }}
                  onClick={handleReset}
                >
                  Reset
                </button>

              </form>{" "}
            </div>
            <div className="smallcaption"></div>
            <hr></hr>
            <div className="postflex d-flex justify-content-evenly  ">
              <div className="iconsHead">
                {/* copyButton */}
                <CopyToClipboard text={shortUrl} onCopy={handleCopy} >
                  <button className="icons icon1" title="copy" size={30}>
                    {copied ? "Copied!" : ""}
                    <FiCopy />
                  </button>
                </CopyToClipboard>

                {/* share */}
                <button className="icons icon2" title="open link">
                  <Link to={shortUrl} target="_blank" size={50}>
                    <RiShareForwardLine />
                  </Link>
                </button>
                {/* mail */}

                <button
                  className="mailButton"
                  onClick={() => setShowInput(!showInput)}
                  title="mail to"
                >
                  <FiMail />
                </button>
                <br />
                {showInput && (
                  <form onSubmit={handleSendtoAddress}>
                    <label>
                      To:
                      <input
                        type="toAddress"
                        value={toAddress}
                        onChange={(e) => settoAddress(e.target.value)}
                      />
                    </label>
                    <button onClick={handleSendtoAddress} type="submit">
                      Send email
                    </button>
                  </form>
                )}

                {/* <input /> */}
              </div>

              <div class="verticalLine "></div>

              <div className="QR col-4 my-4">
                <div className="qrcode ">
                  <QRCode value={qrCodeValue} size={60} />
                </div>
                <span> Scan the QR code </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Box;
