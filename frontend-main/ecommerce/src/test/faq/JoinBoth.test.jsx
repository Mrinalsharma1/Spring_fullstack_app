import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import JoinBoth from '../../components/faq/JoinBoth';
import TrustedClients from '../../components/faq/TrustedClients';
import FAQ from '../../components/faq/FAQ';

jest.mock('../../components/faq/TrustedClients');
jest.mock('../../components/faq/FAQ');

describe('JoinBoth', () => {
  test('renders TrustedClients and FAQ components', () => {
    TrustedClients.mockImplementation(() => <div>TrustedClients Component</div>);
    FAQ.mockImplementation(() => <div>FAQ Component</div>);

    const { getByText } = render(<JoinBoth />);

    expect(getByText('TrustedClients Component')).toBeInTheDocument();
    expect(getByText('FAQ Component')).toBeInTheDocument();
  });
});
