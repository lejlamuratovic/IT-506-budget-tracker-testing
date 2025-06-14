import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { Loading } from "../../components";

describe("Loading Component", () => {
  it("renders a CircularProgress indicator", () => {
    render(<Loading />);

    // Ensure the CircularProgress element is rendered
    const progressIndicator = screen.getByRole("progressbar");
    expect(progressIndicator).toBeInTheDocument();
  });

  it("renders with full height and width", () => {
    render(<Loading />);

    // Check that the parent container has the correct styles
    const boxElement = screen.getByTestId("loading-container");
    expect(boxElement).toHaveStyle({
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      height: "100%",
      width: "100%",
    });
  });

  it("applies primary color to the CircularProgress", () => {
    render(<Loading />);

    // Verify the CircularProgress uses the primary color
    const progressIndicator = screen.getByRole("progressbar");
    expect(progressIndicator).toHaveClass("MuiCircularProgress-colorPrimary");
  });
});
