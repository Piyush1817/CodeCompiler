function DownloadButton(props) {

    function handleDownload() {

        const blob =
            new Blob(
                [props.code],
                {
                    type: "text/plain"
                }
            );

        const url =
            URL.createObjectURL(blob);

        const a =
            document.createElement("a");

        let extension = "txt";

        if (props.language === "java") {
            extension = "java";
        }

        if (props.language === "python") {
            extension = "py";
        }

        if (props.language === "cpp") {
            extension = "cpp";
        }

        a.href = url;

        a.download =
            `code.${extension}`;

        a.click();

        URL.revokeObjectURL(url);
    }

    return (

        <button
            onClick={handleDownload}
        >
            Download
        </button>

    );
}

export default DownloadButton;