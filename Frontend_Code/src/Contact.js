import React, { useState } from "react";
import axios from "axios";

export default function Contact() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [contactNumber, setContactNumber] = useState("");
  const [isCheckboxChecked, setIsCheckboxChecked] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      !firstName ||
      !lastName ||
      !email ||
      !contactNumber ||
      !isCheckboxChecked
    ) {
      return;
    }

    try {
      await axios.post("https://sw8.in/save", {
        firstName,
        lastName,
        contactNumber,
        email,
      });
      setFirstName("");
      setLastName("");
      setEmail("");
      setContactNumber("");
      setIsCheckboxChecked(false);
      console.log("submitted");
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <div className="contactMain">
        <div className="contactTitle">
          <h1>Contact Us</h1>
          <p>
            For any support and queries,
            <br />
            please contact us <br /> Our Experts will contact you
            very soon.
          </p>
          <div className="contactAddress">
            <h5>Our Office Location</h5>
            <p>21136 Castleview<br/>Lake Forest<br/>California-92630</p>
            <p>+1 737-300-7595<br/>
            +91 90870 86286</p>
            <p>contact@foxoneusa.com</p>
          </div>
        </div>

        <div className="contact">
          <form className="contact-form" onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="exampleInputFirstName" className="form-label">
                First Name<small>*</small>
              </label>
              <br />
              <input
                type="text"
                className="firstName"
                id="exampleInputFirstName"
                aria-describedby="nameHelp"
                style={{ width: "100%" }}
                value={firstName}
                onChange={(event) => setFirstName(event.target.value)}
              />
              <br />

              <label htmlFor="exampleInputLastName" className="form-label">
                Last Name<small>*</small>
              </label>
              <br />
              <input
                type="text"
                className="lastName"
                id="exampleInputLastName"
                aria-describedby="nameHelp"
                style={{ width: "100%" }}
                value={lastName}
                onChange={(event) => setLastName(event.target.value)}
              />
              <br />

              <div className="mb-3">
                <label htmlFor="exampleInputContact" className="form-label">
                  Contact Number<small>*</small>
                </label>
                <br />
                <input
                  type="tel"
                  className="contact"
                  id="exampleInputContact"
                  style={{ width: "100%" }}
                  value={contactNumber}
                  onChange={(event) => setContactNumber(event.target.value)}
                />
              </div>

              <label htmlFor="exampleInputEmail" className="form-label">
                Email Address<small>*</small>
              </label>
              <br />
              <input
                type="email"
                className="email"
                id="exampleInputEmail"
                aria-describedby="emailHelp"
                style={{ width: "100%" }}
                value={email}
                onChange={(event) => setEmail(event.target.value)}
              />
            </div>
            <br />
            <div className="mb-3 form-check">
              <input
                type="checkbox"
                className="form-check-input"
                id="exampleCheck1"
                checked={isCheckboxChecked}
                onChange={(event) => setIsCheckboxChecked(event.target.checked)}
              />

              <label
                className="form-check-label"
                htmlFor="exampleCheck1"
                style={{ fontSize: ".7rem" }}
              >
                I, give consent to receive marketing communications from swenfly.
              </label>
            </div>
            <br />
            <button
              type="submit"
              className="btn btn-primary send-message"
              style={{
                width: "100%",
                backgroundColor: "#0d6efd",
                borderRadius: "10px",
                color: "white",
              }}
            >
              Submit
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
