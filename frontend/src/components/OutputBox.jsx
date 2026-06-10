function OutputBox(props) {

    let borderColor = "#444";

    let displayStatus = "";
    if (!props.status) {
    displayStatus = "Ready";
}

    if (props.status === "SUCCESS") {

        borderColor = "green";
        displayStatus = "🟢 Success";

    }

    if (props.status === "COMPILATION_ERROR") {

        borderColor = "red";
        displayStatus = "🔴 Compilation Error";

    }

    if (props.status === "RUNTIME_ERROR") {

        borderColor = "red";
        displayStatus = "🟠 Runtime Error";

    }

    if (props.status === "TIMEOUT") {

        borderColor = "orange";
        displayStatus = "🟡 Timeout";

    }

    if (props.status === "ERROR") {

        borderColor = "red";
        displayStatus = "🔴 Error";

    }

    return (
        <div>

            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginBottom: "10px"
                }}
            >

                <div
                    style={{
                        display: "flex",
                        gap: "15px",
                        alignItems: "center"
                    }}
                >

                    <strong>
                        {displayStatus}
                    </strong>

                    <span>
                        {props.executionTime} ms
                    </span>

                </div>

                <button
                    onClick={() =>
                        navigator.clipboard.writeText(
                            props.output
                        )
                    }
                >
                    Copy
                </button>

            </div>

            <textarea
                rows="10"
                value={
                    props.output ||
                     "Program output will appear here..."
                          }
                readOnly
                style={{
                    width: "100%",
                    border: `2px solid ${borderColor}`
                }}
            />

        </div>
    );
}

export default OutputBox;