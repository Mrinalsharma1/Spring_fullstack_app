import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Header from '../../components/header/Header';

describe('Header', () => {
  test('renders SwiftQSolution logo', () => {
    render(<Header />);
    const logoElement = screen.getByText(/SwiftQSolution/i);
    expect(logoElement).toBeInTheDocument();
  });

  test('renders Opening Time section', () => {
    render(<Header />);
    const openingTimeElement = screen.getByText(/Opening Time/i);
    const openingTimeDetails = screen.getByText(/Allday 9.00 - 18.00/i);
    expect(openingTimeElement).toBeInTheDocument();
    expect(openingTimeDetails).toBeInTheDocument();
  });

  test('renders Email us section', () => {
    render(<Header />);
    const emailElement = screen.getByText(/Email us/i);
    const emailDetails = screen.getByText(/swiftqsolutions@gmail.com/i);
    expect(emailElement).toBeInTheDocument();
    expect(emailDetails).toBeInTheDocument();
  });

  test('renders Call us section', () => {
    render(<Header />);
    const callElement = screen.getByText(/Call us/i);
    const callDetails = screen.getByText(/\+91-8678367823/i);
    expect(callElement).toBeInTheDocument();
    expect(callDetails).toBeInTheDocument();
  });

  test('renders FaRegClock icon', () => {
    render(<Header />);
    const clockIcon = screen.getByRole('img', { hidden: true });
    expect(clockIcon).toBeInTheDocument();
  });

  test('renders IoIosMail icon', () => {
    render(<Header />);
    const mailIcon = screen.getByRole('img', { hidden: true });
    expect(mailIcon).toBeInTheDocument();
  });

  test('renders IoMdCall icon', () => {
    render(<Header />);
    const callIcon = screen.getByRole('img', { hidden: true });
    expect(callIcon).toBeInTheDocument();
  });
});
