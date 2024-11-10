import React from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import {
  FaAirbnb,
  FaFire,
  FaCircleUser,
  FaRegComment,
  FaRibbon,
  FaPhone,
  FaCircleCheck,
  FaRegThumbsUp,
  FaPix,
} from "react-icons/fa6";
import "./Help.css";

function Help() {
  return (
    <DashboardLayout>
      <div className="heading">
        <FaPix className="text mx-2 fs-2" />
        <h2>Frequently Asked Questions</h2>
        <p>These are the most commonly asked questions about SwiftQSolutions</p>
      </div>

      <div>
        <div className="accordion" id="accordionExample">
          <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseOne"
                aria-expanded="true"
                aria-controls="collapseOne"
              >
                <FaAirbnb className="text mx-2 fs-4" /> How can a manager add a
                new time slot for appointments?
              </button>
            </h2>
            <div
              id="collapseOne"
              className="accordion-collapse collapse show"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                The Add Slot component is a React form enabling managers to
                create service appointment slots by selecting a timing and
                displaying a pre-set pincode, streamlining slot management in
                the application.
              </div>
            </div>
          </div>
          <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseTwo"
                aria-expanded="false"
                aria-controls="collapseTwo"
              >
                <FaFire className="text mx-2 fs-4" /> How does manager update
                slot timings?
              </button>
            </h2>
            <div
              id="collapseTwo"
              className="accordsion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                To update a slot's timing, the manager clicks the edit icon,
                enters the new timing, and saves it. The updated timing then
                displays, allowing efficient, inline slot management directly in
                the table.
              </div>
            </div>
          </div>
          <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseThree"
                aria-expanded="false"
                aria-controls="collapseThree"
              >
                <FaCircleUser className="text mx-2 fs-4" /> How can a manager
                mark a booking as completed
              </button>
            </h2>
            <div
              id="collapseThree"
              className="accordion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                The manager can click the "Complete" button in the Action column
                next to each booking. Once clicked, the booking’s status changes
                to "completed," and the button becomes disabled, indicating the
                service is processed.
              </div>
            </div>
          </div>
          <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseFour"
                aria-expanded="false"
                aria-controls="collapseFour"
              >
                <FaRegComment className="text mx-2 fs-4" /> How can a manager
                view the full review content in the Feedbacks section?
              </button>
            </h2>
            <div
              id="collapseFour"
              className="accordion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                In the Feedbacks section, each review is initially truncated. To
                view the full review, click the eye icon in the Actions column
                for the specific feedback entry. This opens a modal dialog
                displaying the entire review content.ls, ensuring high-quality
                service for all your repair needs.
              </div>
            </div>
          </div>
          <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseFive"
                aria-expanded="false"
                aria-controls="collapseFive"
              >
                <FaRibbon className="text mx-2 fs-4" /> How can a manager update
                their profile information in Account Settings?
              </button>
            </h2>
            <div
              id="collapseFive"
              className="accordion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                The manager can click "Edit" to enable the name and email fields
                for updates. After editing, they click "Save" to apply changes
                or "Cancel" to discard them. The role field is view-only.
              </div>
            </div>
          </div>
          {/* hryyyyy */}
          {/* <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseSix"
                aria-expanded="false"
                aria-controls="collapseSix"
              >
                <FaPhone className="text mx-2 fs-4" /> Can I reschedule or
                cancel my booking?
              </button>
            </h2>
            <div
              id="collapseSix"
              className="accordion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                Yes, you can reschedule or cancel your booking up to 24 hours
                before your appointment. Simply log into your account, navigate
                to your booking details, and select the reschedule or cancel
                option. If you need further assistance, feel free to contact our
                support team.
              </div>
            </div>
          </div> */}
          {/* <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseSeven"
                aria-expanded="false"
                aria-controls="collapseSeven"
              >
                <FaCircleCheck className="text mx-2 fs-4" /> What safety
                measures does SwiftQSolutions follow during repairs?
              </button>
            </h2>
            <div
              id="collapseSeven"
              className="accordion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                We prioritize customer safety and follow strict hygiene and
                safety protocols during device repairs. All our technicians are
                trained to handle devices with care, and we ensure proper
                sanitization of the device before and after the repair. We also
                offer contactless drop-off and pick-up services at select
                locations.
              </div>
            </div>
          </div> */}
          {/* <div className="accordion-item">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseEight"
                aria-expanded="false"
                aria-controls="collapseEight"
              >
                <FaRegThumbsUp className="text mx-2 fs-4" />
                How can I contact SwiftQSolutions for support?
              </button>
            </h2>
            <div
              id="collapseEight"
              className="accordion-collapse collapse"
              data-bs-parent="#accordionExample"
            >
              <div className="accordion-body">
                If you have any questions or need assistance with booking a
                repair slot, you can reach us through our support hotline or by
                emailing our customer service team. Additionally, you can use
                the contact form on our website, and a representative will get
                back to you shortly.
              </div>
            </div>
          </div> */}
        </div>
      </div>
    </DashboardLayout>
  );
}

export default Help;
