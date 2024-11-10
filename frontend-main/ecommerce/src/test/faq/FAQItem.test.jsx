import React from "react";
import { render, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import FAQItem from "./FAQItem";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus, faMinus } from "@fortawesome/react-fontawesome";
describe("FAQItem", () => {
  const question = "What is React?";
  const answer = "React is a JavaScript library for building user interfaces.";

  test("renders question and answer", () => {
    const { getByText } = render(
      <FAQItem question={question} answer={answer} />
    );
    expect(getByText(question)).toBeInTheDocument();
    expect(getByText(answer)).toBeInTheDocument();
  });

  test("toggles answer visibility on click", () => {
    const { getByText, queryByText } = render(
      <FAQItem question={question} answer={answer} />
    );
    const questionElement = getByText(question);

    // Initially, the answer should not be visible
    expect(queryByText(answer)).not.toBeVisible();

    // Click to show the answer
    fireEvent.click(questionElement);
    expect(queryByText(answer)).toBeVisible();

    // Click again to hide the answer
    fireEvent.click(questionElement);
    expect(queryByText(answer)).not.toBeVisible();
  });

  test("displays correct icon based on state", () => {
    const { getByText, getByTestId } = render(
      <FAQItem question={question} answer={answer} />
    );
    const questionElement = getByText(question);

    // Initially, the plus icon should be displayed
    expect(getByTestId("icon")).toHaveClass("fa-plus");

    // Click to show the answer and change the icon
    fireEvent.click(questionElement);
    expect(getByTestId("icon")).toHaveClass("fa-minus");

    // Click again to hide the answer and revert the icon
    fireEvent.click(questionElement);
    expect(getByTestId("icon")).toHaveClass("fa-plus");
  });
});
