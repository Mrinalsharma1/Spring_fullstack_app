import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import TrustedClients from '../../components/faq/TrustedClients';

describe('TrustedClients', () => {
  test('renders Trusted Clients heading', () => {
    const { getByText } = render(<TrustedClients />);
    expect(getByText('Trusted Clients')).toBeInTheDocument();
  });

  test('renders client logos and descriptions', () => {
    const { getByAltText, getByText } = render(<TrustedClients />);

    expect(getByAltText('Tamensica')).toBeInTheDocument();
    expect(getByText('The service was exceptional! The team demonstrated great professionalism, providing quick and reliable solutions that exceeded our expectations.')).toBeInTheDocument();

    expect(getByAltText('Rayan')).toBeInTheDocument();
    expect(getByText('Absolutely fantastic experience! The technicians were knowledgeable and completed the repairs efficiently, making it a stress-free process for us.')).toBeInTheDocument();

    expect(getByAltText('Emelli')).toBeInTheDocument();
    expect(getByText('Highly recommend this service! The quality of work was outstanding, and the team was friendly and attentive to our needs throughout the process.')).toBeInTheDocument();
  });

  test('renders FaTools icon', () => {
    const { container } = render(<TrustedClients />);
    const icon = container.querySelector('.fa-tools');
    expect(icon).toBeInTheDocument();
  });
});
