import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import FAQ from '../../components/faq/FAQ'; 

describe('FAQ Component', () => {
  test('renders FAQ header', () => {
    render(<FAQ />);
    const headerElement = screen.getByText(/FAQ/i);
    expect(headerElement).toBeInTheDocument();
  });

  test('renders all FAQ items', () => {
    render(<FAQ />);
    const faqItems = [
      "How to reset a laptop to factory settings?",
      "How to backup data from a damaged phone?",
      "What accessories are covered under warranty?",
      "How to optimize mobile performance?",
      "Can I repair my device under warranty?"
    ];

    faqItems.forEach(question => {
      const questionElement = screen.getByText(question);
      expect(questionElement).toBeInTheDocument();
    });
  });

  test('renders FAQ answers', () => {
    render(<FAQ />);
    const faqAnswers = [
      "To reset a laptop to factory settings, navigate to 'Settings > Update & Security > Recovery' and choose 'Reset this PC'. Follow the on-screen instructions. You can choose to keep your files or remove everything during the reset process. Make sure to back up any important data beforehand, as this action may erase all your files. The reset process could take some time, depending on your system.",
      "You can backup data from a damaged phone by connecting it to a computer and using specialized recovery software. If the screen is unresponsive but the phone still powers on, you may be able to access data via USB. Some recovery tools also allow data extraction from broken devices. It’s a good idea to regularly back up your phone using cloud services to prevent data loss in the future.",
      "Most manufacturers cover chargers and cables under warranty, but physical damage and wear-and-tear are typically not included. Warranty terms vary by brand, so always check the specific details. Batteries may also be covered for a limited period. Items like screen protectors, cases, and third-party accessories are generally not covered under standard warranties.",
      "Clear unnecessary apps and data, update software regularly, and restart the device periodically to optimize mobile performance. You can also disable background app refresh and location services for apps that don’t need them. Managing storage space and uninstalling or disabling unused apps can free up memory and help your phone run faster. Additionally, keeping your phone’s system updated with the latest security patches can enhance performance and security.",
      "Yes, you can repair devices under warranty as long as the damage is not due to misuse or physical damage not covered by the warranty. The warranty usually covers manufacturing defects and malfunctioning components. Make sure to check if the warranty period is still valid. If unauthorized repairs are performed, the warranty may be voided, so it's best to visit an authorized service center."
    ];

    faqAnswers.forEach(answer => {
      const answerElement = screen.getByText(answer);
      expect(answerElement).toBeInTheDocument();
    });
  });

  test('renders FAQ icon', () => {
    render(<FAQ />);
    const iconElement = screen.getByTestId('faq-icon');
    expect(iconElement).toBeInTheDocument();
  });
});
