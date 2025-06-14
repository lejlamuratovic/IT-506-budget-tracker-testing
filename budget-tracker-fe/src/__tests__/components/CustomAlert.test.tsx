import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { CustomAlert } from "../../components";
import userEvent from "@testing-library/user-event";

describe("CustomAlert Component", () => {
  const mockOnClose = jest.fn();

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("renders with default error type and title", () => {
    render(<CustomAlert message="This is an error message." />);
    
    // Verify the alert is rendered
    expect(screen.getByRole("alert")).toBeInTheDocument();

    // Verify the title and message
    expect(screen.getByText("Error")).toBeInTheDocument();
    expect(screen.getByText("This is an error message.")).toBeInTheDocument();
  });

  it("renders with a custom type and title", () => {
    render(
      <CustomAlert
        type="success"
        title="Custom Success"
        message="This is a success message."
      />
    );

    // Verify the alert is rendered
    expect(screen.getByRole("alert")).toBeInTheDocument();

    // Verify the custom title and message
    expect(screen.getByText("Custom Success")).toBeInTheDocument();
    expect(screen.getByText("This is a success message.")).toBeInTheDocument();
  });

  it("renders with default title based on type", () => {
    render(
      <CustomAlert type="info" message="This is an info message." />
    );

    // Verify the default title and message
    expect(screen.getByText("Information")).toBeInTheDocument();
    expect(screen.getByText("This is an info message.")).toBeInTheDocument();
  });

  it("calls onClose when the close button is clicked", async () => {
    render(
      <CustomAlert
        type="warning"
        message="This is a warning message."
        onClose={mockOnClose}
      />
    );

    // Verify the close button is present
    const closeButton = screen.getByRole("button", { name: /close/i });
    expect(closeButton).toBeInTheDocument();

    // Simulate clicking the close button
    await userEvent.click(closeButton);

    // Verify the onClose callback is called
    expect(mockOnClose).toHaveBeenCalledTimes(1);
  });

  it("renders with proper styling", () => {
    render(
      <CustomAlert
        type="success"
        message="Styled success message."
      />
    );

    const alertBox = screen.getByRole("alert");

    // Verify the position and margin styles
    const computedStyle = window.getComputedStyle(alertBox.parentElement!);
    expect(computedStyle.position).toBe("fixed");
    expect(computedStyle.top).toBe("1rem");
    expect(computedStyle.right).toBe("1rem");
    expect(computedStyle.marginBottom).toBe("1rem");
  });
});
